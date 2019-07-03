# chessProgram
This is a chess implementation by Matthew Slesinski.

A summary of the folders so far in the src folder:

boardFeatures:
This contains two main concepts, Lines and Squares. Sqaures are obviously used to represent a board square, and they are the main purpose of this package. Almost all the other enums and classes in this package are used for initializing squares to know which other squares surround them, to provide mechanisms for various utility functions for squares, and are hopefully going to be useful for determining legal moves down the road. File, Rank, and the two diagonal enum types all implement the Line interface. Direction is also an important enum

pieces:
These contain what will be both piece implementations and a useful way of simplifying piece comparisons using an enum.

representation:
This contains useful tools that define a representation of a board

immutableArrayBoard:
The first of the possible actual implementations.


