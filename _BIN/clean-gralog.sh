#!/bin/bash

ANT=ant

rm -rf *.jar plugins
pushd .. > /dev/null
for d in *; do
	if [ -f $d/build.xml ]; then
		echo -n $d
		pushd $d > /dev/null
		${ANT} clean > /dev/null && echo "" || echo " FAILED!"
		popd > /dev/null
	fi
done
popd > /dev/null