#!/bin/bash

ANT=ant

for d in \
	core \
	algorithms \
	app \
	example \
	generator \
	automaton \
	finitegames \
	parity \
	logic \
	two-player-games \
	dagwidth \
	NBA_isEmpty \
; do
	if [ -f ../$d/build.xml ]; then
	
		echo -n $d
		pushd ../$d > /dev/null
		${ANT} jar > /dev/null && echo "" || echo " FAILED"
		popd > /dev/null
	
	fi
done

mkdir -p plugins
for d in ../*; do
	[ -d $d/dist ] && cp -u $d/dist/*.jar plugins/
done
mv plugins/gralog.jar plugins/gralog-algorithms.jar plugins/gralog-core.jar ./
