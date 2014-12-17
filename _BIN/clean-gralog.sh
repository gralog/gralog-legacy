#!/bin/bash

ANT=ant

rm -rf *.jar plugins
for d in ../*; do
	if [ -f $d/build.xml ]; then
		pushd $d
		${ANT} clean
		popd
	fi
done
