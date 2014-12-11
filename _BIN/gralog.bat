@echo off


del *.jar
cd plugins
del *.jar
cd..


copy ..\app\dist\gralog.jar gralog.jar
copy ..\algorithms\dist\gralog-algorithms.jar gralog-algorithms.jar
copy ..\core\dist\gralog-core.jar gralog-core.jar

copy ..\automaton\dist\gralog-automaton.jar plugins\gralog-automaton.jar
copy ..\dagwidth\dist\gralog-dag-width.jar plugins\gralog-dag-width.jar
copy ..\example\dist\gralog-examples.jar plugins\gralog-examples.jar
copy ..\finitegames\dist\gralog-finite-games.jar plugins\gralog-finite-games.jar
copy ..\generator\dist\gralog-generator.jar plugins\gralog-generator.jar
copy ..\logic\dist\gralog-logic.jar plugins\gralog-logic.jar
copy ..\NBA_isEmpty\dist\gralog-nba.jar plugins\gralog-nba.jar
copy ..\parity\dist\gralog-parity.jar plugins\gralog-parity.jar
copy ..\two-player-games\dist\gralog-two-player-games.jar plugins\gralog-two-player-games.jar


java -cp gralog-core.jar;^
gralog-algorithms.jar;^
gralog.jar;^
plugins\gralog-automaton.jar;^
plugins\gralog-dag-width.jar;^
plugins\gralog-examples.jar;^
plugins\gralog-finite-games.jar;^
plugins\gralog-generator.jar;^
plugins\gralog-logic.jar;^
plugins\gralog-nba.jar;^
plugins\gralog-parity.jar;^
plugins\gralog-two-player-games.jar;^
 de.hu.gralog.Gralog
