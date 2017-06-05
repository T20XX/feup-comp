# Project Title: PAT

## Group: G8

NAME1: João Filipe Costa, NR1: up201403967, GRADE1: 20, CONTRIBUTION1: 33.3%
NAME2: José Aleixo Cruz, NR2: up201403526, GRADE2: 19, CONTRIBUTION2: 33.3%
NAME3: Telmo Barros, NR3: up201405840, GRADE3: 20, CONTRIBUTION3: 33.3%

## Summary

For this project, our group made a Java tool, capable of detecting if certain patterns occur in a .java file code. A custom grammar was developed to allow the user to write the pattern that should be found. The matching is done by comparing the .java AST and our own grammar AST.

## Execute

To execute the GUI of the program, give permissions to run the "run.sh" script and then run it.

## Dealing with syntatic errors

For syntatic errors, in our GUI, we show a dialog message telling the user to refer to our syntax documenation. We allow the program to continue running, but no further processing is done, until there are no syntatic errors.

## Semanatic analysis

We do not make any sort of semantic analysis. Since the pattern written by the user must match a .java file code, the semantic analysis is done by the compiler that checks the .java file validity.

## Intermediate representations

We have a IR based on the Java IR. Using the plugin AST-View, we were able to determine the IR the Eclipse AST Parser uses when making code analysis. Our grammar creates an intermediate representation of the code that is heavily similiar to this one, with the addition of our "Pattern" node type. 

The IR is an Abstract Syntax Tree. Each tree node type refers to what the node represents in Java code. For example, the node 'ForStatement' represents everything that a 'for' instruction uses: the pre-instructions, the condition, the post-instructions and the block of code to be executed. 

The IR generated depends on the .java file code and on the rules written for the patterns. 

## Code generator

Our program does not generate any code.

## Overview

Since our application allows an user to search for non-hardcoded Java statements, we developed a new grammar, in order to read the input from the user. With JavaCC we created a parser capable of detecting syntactic errors and of creating an Abstract Syntax Tree of the input, according to the grammar. This grammar can be examined inside the 'patternsGrammar' package.

To pass the AST to a programatically accessible class, we crated a custom node class called 'BasicNode'. The AST that JavaCC delievers is in String format, so we parse that String into a tree composed by 'BasicNode' objects. Each type of node in the JavaCC AST is associated with a class that extends 'BasicNode'. These classes are declared on the 'patternsParser' package.

With the 'BasicNode' tree created, we generate another AST tree, this time from the .java code file that on which we want to find the patterns. This is done using ASTParser, which is an API that Eclipse uses to analyse Java code. Since ASTParser's AST structure is not the same as ours, we created a 'visitor', which iterates through each of the ASTParser's AST and compares it to the 'BasicNode' AST. 

This visitor is defined in the 'MyASTVisitor' class and is the most difficult part of the applcation. Since it has to be coded by hand, the visitor does not support all Java commands, only the most important ones.

A 'MyASTVisitor' object has a 'found' attribute, which is true if the visitor has found a match or false if it hasn't. If one rule has all its visitors return true, then the rule exists in the analysed Java code.

## Examples

The examples folder has 3 types of files: patterns, readme and a test.java file. An example is reproduced by executing the program with the pattern input file argument being the 'x-pattern.txt' (x is 1-5) and with the java file 'test.java'. The 'x-readme.txt' file explains the relevance of the respective pattern.

## Task distribuition

João Filipe was mainly assigned with the construction of our own grammar and the JavaCC parser which validates it. 
Telmo Barros and José Aleixo worked on the matching of the AST genereated from the user input and the .java file AST.

## Pros
- Allows to search for an unknown and not only hard-coded Java
- Allows to search more than one group of patterns (rule) at once
- Highlights the patterns directly in the .java file code

## Cons
- Since the grammar is custom-made, it does not cover all of Java grammar.


