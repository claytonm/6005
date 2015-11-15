# 6.005

Solutions to the Fall 2011 iteration of the MIT Course Elements of Software Construction.

## Problem Set 0: Setup
Setup environment and learn syntax and Java project structure. Wrote class to parse course homepage HTML, extract'Course Element' headers, and return a boolean to indicate whether a given string was among course elements.

## Problem Set 1: Test-first programming
Write black-box tests based on specifications, prior to writing code. Wrote classes to convert digits of pi between bases, express digits in base-26, convert base-26 digits into characters, and ultimately find English words in the resulting string of characters. 

## Problem Set 2: Specifications and designing with state
Write method specifications and tests for those specifications. Use state machines to design a midi piano. Application allows user to play notes on keyboard, change instruments and octaves, and record and play back notes.   

## Problem Set 3: Mutability
The theme this week is mutation. To illustrate, we design a simple calculator interpreter than can handle unit conversion as well as standard arithmetic. The interpreter has three parts. The first is a Lexer that converts user input into a stream of tokens. If a user enters (3PT+2)/5, then the Lexer converts this into an object that returns the tokens of this expression via its next() method. In this example, the tokens would be (, 3PT, +, 2.0, ), /, and 5. The second part is a recursive descent parser that converts this stream of tokens into a tree structure that reflects the order of operations. The third piece is an evaluator that takes the parsed expression as input and returns an answer in appropriate units. 

Mutability enters with the Lexer object, which is mutated as each call to its next() method peels off another token.

Students of 6.001 or readers of SICP will recognize the similarity between this project and the piece de resistance of that course, the design of a Scheme interperter.

## Problem Set 4: Immutability
Contra PS3, in this problem set we use immutable objects to design a SAT solver for Sudoku. This involves defining a Sudoku class and it rep invariants, converting Sudoku into a logical expression in conjuctive normal form, solving that expression through a recursive backtracking algorithm, and then converting the solution into a completed sudoku puzzle.

