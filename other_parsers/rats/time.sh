#!/bin/bash
time for i in {1..1000}; do java -ea -cp .:rats.jar SimpleMain ../../in.txt; done;
