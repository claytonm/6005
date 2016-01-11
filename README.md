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

## Project 1: ABC Music Player
ABC is a music notation that can be encoded in a standard text file. For example, ^F1/2 is an F-sharp half-note, [CEG] is a C-major chord, (3_AB^C is A-flat, B, and C-sharp played in a triplet, and z is a quarter-note rest. This project involves the design from scratch of a system to parse and play ABC files. This requires:

1) A system to represent musical objects. For example, the one property that notes, rests, chords, and triplets all have in common is that they persist in time. They can therefore each be implemented as extensions of an abstract note class that contains only a duration variable. 

2) A system to read in ABC files, tokenize the symbols line-by-line, and then parse the stream of tokens into an abstract syntax tree. The syntax tree reflects the structure of the music in terms of repetitions, multiple-voices, etc. The nodes of the tree - the smallest unit of musical structure - are bars. The bar object class contains static variables that contain the key and time signature of the piece, and instance variables that contain any accidentals that occur in the bar itelf.

3) A class to traverse the syntax tree, and make the appropriate call to the play method in Java's MIDI Sequence class. Different note objects are played differently: notes are simply played at a certain pitch for a certain duration, rests are not played at all but should advance the time forward, chords begin multiple notes at the same time and endure for the length of their longest note. We create a visitor class to handle these cases with a single function. The ToPitch class implements the Visitor class with method signatures for notes, rests, chords, and triplets, each of which evokes Java's MIDI play method in the appropriate way.

This project synthesized all perviously class material and required a substantial amount of design and planning. Plan before you code!

## Problem Set 5: Networks
This problem set is about distributing computations over a network through processes and sockets. We make two client/server programs. The first is a simple echo server that prints back input from the user verbatim. The second is a more interesting protocol: the client accepts an integer as input from the user, connects to several servers, and distributes prime number finding to each of those servers.  
