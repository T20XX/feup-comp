#COMPILE
echo "Compiling..."

#COMPILE JAVACC
javacc -OUTPUT_DIRECTORY="src/patternsGrammar" ./src/patternsGrammar/Parser.jj

#COMPILE JAVA
mkdir -p ./bin
javac -d "bin" -cp "lib/gson-2.8.0.jar" ./src/PAT.java ./src/javaCode/*.java ./src/patternsGrammar/*.java

#EXECUTE PAT
echo "Running..."
java -cp "bin:lib/gson-2.8.0.jar" PAT patterns.txt TestSpoon.java
