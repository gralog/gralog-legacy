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
	
		pushd ../$d
		${ANT} jar
		popd
	
	fi
done

mkdir -p plugins
for d in ../*; do
	[ -d $d/dist ] && cp -u $d/dist/*.jar plugins/
done
mv plugins/gralog.jar plugins/gralog-algorithms.jar plugins/gralog-core.jar ./
