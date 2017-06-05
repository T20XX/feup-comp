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



## Testsuite and test infrastricture

## Task distribuition

João Filipe was mainly assigned with the construction of our own grammar and the JavaCC parser which validates it. Telmo Barros and José Aleixo worked on the matching of the AST genereated from the user input and the .java file AST.

## Pros

## Cons


