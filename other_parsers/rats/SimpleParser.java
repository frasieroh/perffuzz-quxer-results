// ===========================================================================
// This file has been generated by
// Rats! Parser Generator, version 2.4.0,
// (C) 2004-2014 Robert Grimm,
// on Friday, April 3, 2020 at 2:28:55 AM.
// Edit at your own risk.
// ===========================================================================

import java.io.Reader;
import java.io.IOException;

import xtc.util.Pair;

import xtc.parser.ParserBase;
import xtc.parser.Column;
import xtc.parser.Result;
import xtc.parser.SemanticValue;
import xtc.parser.ParseError;

/**
 * Packrat parser for grammar <code>calc</code>.
 *
 * <p />This class has been generated by the <i>Rats!</i> parser
 * generator, version 2.4.0, (C) 2004-2014 Robert Grimm.
 */
public final class SimpleParser extends ParserBase {

  // =========================================================================

  /** Memoization table column. */
  static final class SimpleParserColumn extends Column {
    Result fexpression;
    Result fadd;
    Result fmul;
    Result fterm;
    Result fterm$$Plus1;
    Result fws;
  }

  // =========================================================================

  /**
   * Create a new packrat parser.
   *
   * @param reader The reader.
   * @param file The file name.
   */
  public SimpleParser(final Reader reader, final String file) {
    super(reader, file);
  }

  /**
   * Create a new packrat parser.
   *
   * @param reader The file reader.
   * @param file The file name.
   * @param size The file size.
   */
  public SimpleParser(final Reader reader, final String file, final int size) {
    super(reader, file, size);
  }

  // =========================================================================

  protected Column newColumn() {
    return new SimpleParserColumn();
  }

  // =========================================================================

  /**
   * Parse nonterminal calc.statement.
   *
   * @param yyStart The index.
   * @return The result.
   * @throws IOException Signals an I/O error.
   */
  public Result pstatement(final int yyStart) throws IOException {
    int        yyC;
    Result     yyResult;
    boolean    yyPredMatched;
    Void       yyValue;
    ParseError yyError = ParseError.DUMMY;

    // Alternative 1.

    yyResult = pws(yyStart);
    yyError  = yyResult.select(yyError);
    if (yyResult.hasValue()) {

      yyResult = pexpression(yyResult.index);
      yyError  = yyResult.select(yyError);
      if (yyResult.hasValue()) {
        Integer e = yyResult.semanticValue();

        yyResult = pws(yyResult.index);
        yyError  = yyResult.select(yyError);
        if (yyResult.hasValue()) {

          yyPredMatched = false;

          yyC = character(yyResult.index);
          if (-1 != yyC) {

            yyPredMatched = true;
          }

          if (! yyPredMatched) {

            System.out.println(e);

            yyValue = null;

            return yyResult.createValue(yyValue, yyError);
          } else {
            yyError = yyError.select("statement expected", yyStart);
          }
        }
      }
    }

    // Alternative 2.

    System.out.println("error");

    yyValue = null;

    return new SemanticValue(yyValue, yyStart, yyError);
  }

  // =========================================================================

  /**
   * Parse nonterminal calc.expression.
   *
   * @param yyStart The index.
   * @return The result.
   * @throws IOException Signals an I/O error.
   */
  private Result pexpression(final int yyStart) throws IOException {
    SimpleParserColumn yyColumn = (SimpleParserColumn)column(yyStart);
    if (null == yyColumn.fexpression) 
      yyColumn.fexpression = pexpression$1(yyStart);
    return yyColumn.fexpression;
  }

  /** Actually parse calc.expression. */
  private Result pexpression$1(final int yyStart) throws IOException {
    Result     yyResult;
    Integer    yyValue;
    ParseError yyError = ParseError.DUMMY;

    // Alternative 1.

    yyResult = padd(yyStart);
    yyError  = yyResult.select(yyError);
    if (yyResult.hasValue()) {
      yyValue = yyResult.semanticValue();

      return yyResult.createValue(yyValue, yyError);
    }

    // Done.
    return yyError;
  }

  // =========================================================================

  /**
   * Parse nonterminal calc.add.
   *
   * @param yyStart The index.
   * @return The result.
   * @throws IOException Signals an I/O error.
   */
  private Result padd(final int yyStart) throws IOException {
    SimpleParserColumn yyColumn = (SimpleParserColumn)column(yyStart);
    if (null == yyColumn.fadd) yyColumn.fadd = padd$1(yyStart);
    return yyColumn.fadd;
  }

  /** Actually parse calc.add. */
  private Result padd$1(final int yyStart) throws IOException {
    int        yyC;
    int        yyIndex;
    Result     yyResult;
    Integer    yyValue;
    ParseError yyError = ParseError.DUMMY;

    // Alternative 1.

    yyResult = pmul(yyStart);
    yyError  = yyResult.select(yyError);
    if (yyResult.hasValue()) {
      Integer l = yyResult.semanticValue();

      final int yyChoice1 = yyResult.index;

      // Nested alternative 1.

      yyC = character(yyChoice1);
      if (-1 != yyC) {
        yyIndex = yyChoice1 + 1;

        switch (yyC) {
        case '+':
          {
            yyResult = padd(yyIndex);
            yyError  = yyResult.select(yyError);
            if (yyResult.hasValue()) {
              Integer r = yyResult.semanticValue();

              yyValue = l + r;

              return yyResult.createValue(yyValue, yyError);
            }
          }
          break;

        case '-':
          {
            yyResult = padd(yyIndex);
            yyError  = yyResult.select(yyError);
            if (yyResult.hasValue()) {
              Integer r = yyResult.semanticValue();

              yyValue = l - r;

              return yyResult.createValue(yyValue, yyError);
            }
          }
          break;

        default:
          /* No match. */
        }
      }
    }

    // Alternative 2.

    yyResult = pmul(yyStart);
    yyError  = yyResult.select(yyError);
    if (yyResult.hasValue()) {
      yyValue = yyResult.semanticValue();

      return yyResult.createValue(yyValue, yyError);
    }

    // Done.
    yyError = yyError.select("add expected", yyStart);
    return yyError;
  }

  // =========================================================================

  /**
   * Parse nonterminal calc.mul.
   *
   * @param yyStart The index.
   * @return The result.
   * @throws IOException Signals an I/O error.
   */
  private Result pmul(final int yyStart) throws IOException {
    SimpleParserColumn yyColumn = (SimpleParserColumn)column(yyStart);
    if (null == yyColumn.fmul) yyColumn.fmul = pmul$1(yyStart);
    return yyColumn.fmul;
  }

  /** Actually parse calc.mul. */
  private Result pmul$1(final int yyStart) throws IOException {
    int        yyC;
    int        yyIndex;
    Result     yyResult;
    Integer    yyValue;
    ParseError yyError = ParseError.DUMMY;

    // Alternative 1.

    yyResult = pterm(yyStart);
    yyError  = yyResult.select(yyError);
    if (yyResult.hasValue()) {
      Integer l = yyResult.semanticValue();

      final int yyChoice1 = yyResult.index;

      // Nested alternative 1.

      yyC = character(yyChoice1);
      if (-1 != yyC) {
        yyIndex = yyChoice1 + 1;

        switch (yyC) {
        case '*':
          {
            yyResult = pmul(yyIndex);
            yyError  = yyResult.select(yyError);
            if (yyResult.hasValue()) {
              Integer r = yyResult.semanticValue();

              yyValue = l * r;

              return yyResult.createValue(yyValue, yyError);
            }
          }
          break;

        case '/':
          {
            yyResult = pmul(yyIndex);
            yyError  = yyResult.select(yyError);
            if (yyResult.hasValue()) {
              Integer r = yyResult.semanticValue();

              yyValue = l / r;

              return yyResult.createValue(yyValue, yyError);
            }
          }
          break;

        default:
          /* No match. */
        }
      }
    }

    // Alternative 2.

    yyResult = pterm(yyStart);
    yyError  = yyResult.select(yyError);
    if (yyResult.hasValue()) {
      yyValue = yyResult.semanticValue();

      return yyResult.createValue(yyValue, yyError);
    }

    // Done.
    yyError = yyError.select("mul expected", yyStart);
    return yyError;
  }

  // =========================================================================

  /**
   * Parse nonterminal calc.term.
   *
   * @param yyStart The index.
   * @return The result.
   * @throws IOException Signals an I/O error.
   */
  private Result pterm(final int yyStart) throws IOException {
    SimpleParserColumn yyColumn = (SimpleParserColumn)column(yyStart);
    if (null == yyColumn.fterm) yyColumn.fterm = pterm$1(yyStart);
    return yyColumn.fterm;
  }

  /** Actually parse calc.term. */
  private Result pterm$1(final int yyStart) throws IOException {
    int        yyC;
    int        yyIndex;
    Result     yyResult;
    int        yyBase;
    Integer    yyValue;
    ParseError yyError = ParseError.DUMMY;

    // Alternative 1.

    yyResult = pterm$$Plus1(yyStart);
    yyError  = yyResult.select(yyError);
    if (yyResult.hasValue()) {
      Pair<Character> digits = yyResult.semanticValue();

      Integer total = 0;
      for (Character elem : digits) {
        total = 10 * total + Character.getNumericValue(elem);
      }
      yyValue = total;

      return yyResult.createValue(yyValue, yyError);
    }

    // Alternative 2.

    yyC = character(yyStart);
    if ('(' == yyC) {
      yyIndex = yyStart + 1;

      yyResult = pexpression(yyIndex);
      yyError  = yyResult.select(yyError);
      if (yyResult.hasValue()) {
        yyValue = yyResult.semanticValue();

        yyBase = yyResult.index;
        yyC    = character(yyBase);
        if (')' == yyC) {
          yyIndex = yyResult.index + 1;

          return new SemanticValue(yyValue, yyIndex, yyError);
        } else {
          yyError = yyError.select("')' expected", yyBase);
        }
      }
    }

    // Done.
    yyError = yyError.select("term expected", yyStart);
    return yyError;
  }

  // =========================================================================

  /**
   * Parse synthetic nonterminal calc.term$$Plus1.
   *
   * @param yyStart The index.
   * @return The result.
   * @throws IOException Signals an I/O error.
   */
  private Result pterm$$Plus1(final int yyStart) throws IOException {
    SimpleParserColumn yyColumn = (SimpleParserColumn)column(yyStart);
    if (null == yyColumn.fterm$$Plus1) 
      yyColumn.fterm$$Plus1 = pterm$$Plus1$1(yyStart);
    return yyColumn.fterm$$Plus1;
  }

  /** Actually parse calc.term$$Plus1. */
  private Result pterm$$Plus1$1(final int yyStart) throws IOException {
    int             yyC;
    int             yyIndex;
    Result          yyResult;
    Pair<Character> yyValue;
    ParseError      yyError = ParseError.DUMMY;

    // Alternative 1.

    yyC = character(yyStart);
    if (-1 != yyC) {
      yyIndex = yyStart + 1;
      char v$el$1 = (char)yyC;

      if (('0' <= v$el$1) && (v$el$1 <= '9')) {

        final int yyChoice1 = yyIndex;

        // Nested alternative 1.

        yyResult = pterm$$Plus1(yyChoice1);
        yyError  = yyResult.select(yyError);
        if (yyResult.hasValue()) {
          Pair<Character> v$2 = yyResult.semanticValue();

          yyValue = new Pair<Character>(v$el$1, v$2);

          return yyResult.createValue(yyValue, yyError);
        }

        // Nested alternative 2.

        yyValue = new Pair<Character>(v$el$1);

        return new SemanticValue(yyValue, yyChoice1, yyError);
      }
    }

    // Done.
    yyError = yyError.select("term expected", yyStart);
    return yyError;
  }

  // =========================================================================

  /**
   * Parse nonterminal calc.ws.
   *
   * @param yyStart The index.
   * @return The result.
   * @throws IOException Signals an I/O error.
   */
  private Result pws(final int yyStart) throws IOException {
    SimpleParserColumn yyColumn = (SimpleParserColumn)column(yyStart);
    if (null == yyColumn.fws) yyColumn.fws = pws$1(yyStart);
    return yyColumn.fws;
  }

  /** Actually parse calc.ws. */
  private Result pws$1(final int yyStart) throws IOException {
    int        yyC;
    int        yyIndex;
    Result     yyResult;
    Void       yyValue;
    ParseError yyError = ParseError.DUMMY;

    // Alternative 1.

    yyC = character(yyStart);
    if (-1 != yyC) {
      yyIndex = yyStart + 1;

      switch (yyC) {
      case '\t':
      case '\n':
      case ' ':
        {
          yyResult = pws(yyIndex);
          yyError  = yyResult.select(yyError);
          if (yyResult.hasValue()) {

            yyValue = null;

            return yyResult.createValue(yyValue, yyError);
          }
        }
        break;

      default:
        /* No match. */
      }
    }

    // Alternative 2.

    yyValue = null;

    return new SemanticValue(yyValue, yyStart, yyError);
  }

}
