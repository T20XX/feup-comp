#COMPILE
echo "Compiling..."

#COMPILE JAVACC
javacc -OUTPUT_DIRECTORY="src/patternsGrammar" ./src/patternsGrammar/Parser.jj

#COMPILE JAVA
mkdir -p ./bin
javac -g -d "bin" -cp "lib/*" ./src/PAT.java ./src/jsonParser/adapters/*.java ./src/jsonParser/containers/*.java ./src/patternsGrammar/*.java

#EXECUTE PAT
echo "Running..."
java -cp "bin:lib/*" PAT patterns.txt TestSpoon.java
