Requirements:
- Java 1.8

To compile:
mkdir build
javac -sourcepath src -d build src/**/*.java

mkdir build/src
cp -r src/assets build/src

To run:
cd build
java gui.HeartsFrame

This will open the GUI. Press play to watch the games run. In the options menu you can select to run multiple trials which will be run in the background using the players.
Click change players to change the players. The run multiple trials will run the number of games (between 1 and 1000) using the current players in the background and save the results to the specified file.
