#!/bin/bash

CLASSPATH=$( ls -1 *.jar plugins/*.jar | tr "\\n" ":" ).
java -cp "$CLASSPATH" de.hu.gralog.Gralog