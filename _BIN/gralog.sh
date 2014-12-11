#!/bin/bash

rm *.jar plugins/*.jar
if [ ! -d plugins ]; then
	mkdir plugins
fi

for d in ../*; do
	if [ -d $d/dist ]; then
		cp $d/dist/*.jar plugins/
	fi
done

mv plugins/gralog.jar \
   plugins/gralog-algorithms.jar \
   plugins/gralog-core.jar \
   ./

CLASSPATH=$( ls -1 *.jar plugins/*.jar | tr "\\n" ":" ).
java -cp "$CLASSPATH" de.hu.gralog.Gralog