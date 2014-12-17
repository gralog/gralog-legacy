#!/bin/bash

mkdir -p plugins
rm -f *.jar plugins/*.jar
for d in ../*; do
	[ -d $d/dist ] && cp -u $d/dist/*.jar plugins/
done
mv plugins/gralog.jar plugins/gralog-algorithms.jar plugins/gralog-core.jar ./
java -cp "$( ls -1 *.jar plugins/*.jar | tr "\\n" ":" )" de.hu.gralog.Gralog
