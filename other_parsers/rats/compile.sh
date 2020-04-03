#!/bin/sh

java -ea -cp rats.jar xtc.parser.Rats -lgpl calc.rats
javac -g -cp rats.jar SimpleParser.java SimpleMain.java
