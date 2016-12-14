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