Running cache/callgrind on quxer proved that memory allocation was a serious
bottleneck. Malloc generally works well for what it needs to do, but with
domain-specific knowledge it leaves some extra performance on the table.

## Allocation

If a parse fails, the entire current subtree needs to be freed. This can be 
done very efficiently by using a stack allocator and manipulating its stack
pointer. However, naive stack allocation does not work, because parse tree
nodes are not fixed in size (they can have an arbitrary number of children);
and because of memoizaiton, every subtree of a nonterminal ought to be cached
and not overwritten even if it is the subtree of a failed parse.

This is solved by allocating fixed-size chunks for nodes below a specific
size (48 bytes, up to 3 children) and arbitrary-sized chunks in a seperate
region for larger allocations. The arbitrary-size allocator is just an ad-hoc 
next-fit allocator. It proved to be no faster than malloc because of its poor
cache locality - reading and writing headers around allocations tended to
produce cache misses. Malloc itself is a more well-engineered version of what
I wrote, so none of this is all that surprising.

The fixed size allocator requires less bookkeeping; furthermore, allocation
headers could be moved into a second region where they sat adjacent to one
another. Allocating a chunk in the ideal case only requires reading and writing
the current and next header, and since a few headers fit easily within a single
cache line locality is much improved.

These headers keep track of which nodes are memoized. Before something is
allocated, it verifies the node at that memory location is not memoized;
if it is, it sets the stack pointer to its location when the node was cached,
thereby skipping it. 

## Comparison versus other parsers

It appears that quxer compares favorably with other parsers with respect to
performance. It would be worrying if this weren't the case since it's written
in C, templates effectively inline everything, and there is (almost) no
run-time checking.

The simple grammar below is used to implement a basic calculator (note:
operators are right-associative because quxer does not support left recursion):
```
~statement = ws e : expression ws eol {{ printf("answer=%d\n", *e); }}
           / ( !eol . )* eol {{ printf("error\n"); }}

~expression = e : add {{ result = *e; }}

~add  = l : mul "+" r : add {{ result = *l + *r; }}
      / l : mul "-" r : add {{ result = *l - *r; }}
      / o : mul {{ result = *o; }}

~mul  = l : term "*" r : mul {{ result = *l * *r; }}
      / l : term "/" r : mul {{ result = *l / *r; }}
      / o : term {{ result = *o; }}

~term = <[0-9]+> {{ result = atoi(c(0)); }}
      / "(" e : expression ")" {{ result = *e; }}

~ws    = [ \t]*

~eol    = "\n" / "\r\n" / "\r" / ";"
```
This was transpiled with `quxer calc.txt -d int`. Modified grammar files 
are present in the `other_parsers` directory compatible with the respective
program. Then each program was fed `in.txt` 1000 times, a simple but very long
mathematical expression. This was timed with Linux `time`. The handwritten
allocator described above is `arena`, the original `malloc`. Results:

- quxer (arena, -O2): 0.037321 sec/exec
- quxer (malloc, -O2): 0.047395 sec/exec
- PackCC (-O2): 0.099878 sec/exec
- Rats! (JVM): 0.135383 sec/exec

## PerfFuzz

PerfFuzz is used to determine pathological inputs for arbitrary programs.
Running this on quxer yields both syntactically valid and syntactically invalid
inputs, each of which create distinct hotspots. The inputs were limited to
256 bytes. Fuzzing took place for ~24 hours, and the results
are in `arene_results/queue` and `master_results/queue`. Here are some
interesting ones: 

`master_results/queue/id:005046,src:005044,op:arith8,pos:243,val:+20,+max`:
```
~_=_	<<<_	<<_	<<<_	<<_	<_	<<_	<<<_	<_
<_	<_	<<_	<_	<_	<_	<_	<_	<_
<_	<_	<_	<_	<_	<_	<<_	<_	<_
<_	<_	<_	<<<_	<_	<_	<_	<_	<_
<_	<_	<<_	<<<_	<_	<<<_	<_	<<_	<<_
<_	<<_	<_	<<_	<<_	<_	<<_	<_	<_
<_	<<_	<<_	<_	<<_	<_	<<_	<<_	<<_
<_	<<_	<<_	<<_	<_	<<_	<_	<_	<<_
<_
```
Newlines mine. Looking at the PEG metagrammar `peg.txt` shows what's going on:
```
~group       = "(" ws o : body ws ")" {{ result = *o; }}
             / "<" ws o : body ws ">" {{ result = ast_Capture(*o); }}
             / o : final {{ result = *o; }}
```
Each open angle bracket begins a capture (note it must fail to parse "(" first).
This succeeds until the parser reaches EOF, after which every capture fails to
parse bottom-up. Also of note is the `ws` grammar rule:
```
~ws          = (" " / "\n" / "\t")*
```
The tab character is attempted last, so this input stresses the no-parse
case of `" "` and `\n`.

`arena_results/queue/id:005696,src:005690,op:havoc,rep:4,+max`:
```
~dJ =. . <. . . . /. . . <. /. . . <. . . .
. . .   . . <. . /. . . .
. . . . <
. . . . .
.  . <g <g <. . . . . . . .
. . .   <. . . . <. .
. . . <. . . . . . . . . .
. . . . . . . <. . .
O <. <. . . .
. .  0g <. < . . <. . . .
. . . <. .  . <. . <. . . .
```
This create a hotspot on `eval_dot` corresponding to the `~dot` grammar
rule. 

`arena_results/queue/id:005660,src:005652,op:flip4,pos:197,+max`
```
~g  =w/g/r/J/g/J/g/ m/o/J/g/J/m/r/J/g/J/J/8/J/g/J/r/JW/g/X/J/J/g/m/r/J/J/8/r/
J/g/Z/g/m*/m/r/J/g/J/8/J/g/J/m/r/J/g/J/8/J/g/J/g/J/8/g/6?/g/m8/J/g/J/m/r/J/g/
g/Jg/m/r/J/J/8/q/J/g/J/8 /g/J/8/J/g/J/g/J/8/<r/r/J/g/g/J/8/J/g/J/m/rJ/ g/J/m/
r/J/g/g/Jgg/J/8/JJ/8/J/
```
Newlines mine. As above, but for `eval_alt` corresponding to the `~alt` grammar
rule.

Although obvious in retrospect, PerfFuzz will produce inputs that
highlight a single grammar rule.
