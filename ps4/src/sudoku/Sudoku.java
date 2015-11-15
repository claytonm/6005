/**
 * Author: dnj, Hank Huang
 * Date: March 7, 2009
 * 6.005 Elements of Software Construction
 * (c) 2007-2009, MIT 6.005 Staff
 */
package sudoku;

import java.io.IOException;

import sat.env.Environment;
import sat.env.Variable;
import sat.env.Bool;
import sat.formula.*;

import java.util.*;
import java.nio.file.*;

/**
 * Sudoku is an immutable abstract datatype representing instances of Sudoku.
 * Each object is a partially completed Sudoku puzzle.
 */
public class Sudoku {
    // dimension: standard puzzle has dim 3
    private final int dim;
    // number of rows and columns: standard puzzle has size 9
    private final int size;
    // known values: square[i][j] represents the square in the ith row and jth
    // column,
    // contains -1 if the digit is not present, else i>=0 to represent the digit
    // i+1
    // (digits are indexed from 0 and not 1 so that we can take the number k
    // from square[i][j] and
    // use it to index into occupies[i][j][k])
    private final int[][] square;
    // occupies [i,j,k] means that kth symbol occupies entry in row i, column j
    private final Variable[][][] occupies;

    // Rep invariant
    // for m, n in [1,2,3], each k occurs at most once in the square[3*(m-1) <= i <= 3*m][3*(n-1) <= j <= 3*n]
    // for m in [0,1,size-1], each k occurs at most once in the row square[m,j]
    // for m in [0,1,size-1], each k occurs at most once in the row square[j,m]

    private void assertUnique(int[] elements) {
        // asserts that each element in elements occurs at most once
        // elements.length = size
        // elements contains only integers i, where 0 <= i < size, or i == -1
        int i = 0;
        int[] counts = new int[size];
        while(i < size) {
            // counts contains the number of times each element
            // appears in elements
            if (elements[i] != -1) {counts[elements[i]] = 1 + counts[elements[i]];}
            i++;
        }

        i = 0;
        while (i < size) {
            // assert that each element appears at most once
            assert counts[i] <= 1;
            i++;
        }
    }

    private int[] getColumn(int[][] table, int colIndex) {
        // returns column in position colIndex in table
        int i = 0;
        int[] col = new int[size];
        while (i < size) {
            col[i] = table[i][colIndex];
            i++;
        }
        return col;
    }

    private int[] getRegion(int[][] table, int dim, int colIndex, int rowIndex) {
        // colIndex and rowIndex are the coordinates of the
        // upper-left square of the requested subRegion
        // size mod colIndex == size mod rowIndex == 0;
        int colIndexFinal = colIndex + dim;
        int rowIndexFinal = rowIndex + dim;
        int row = 0;
        int col = 0;
        int[] subRegion = new int[size];
        int[][] subRows = new int[dim][dim];
        while (row < dim) {
            subRows[row] = table[row + rowIndex];
            row++;
        }
        row = 0;
        int k = 0;
        while (row < dim) {
            int[] currentRow = subRows[row];
            while (col < dim) {
                subRegion[k] = currentRow[col + colIndex];
                col++;
                k++;
            }
            col = 0;
            row++;
        }
        return(subRegion);
    }

    private int[] getRow(int[][] table, int rowIndex) {
        // returns row in position rowIndex in table
        return table[rowIndex];
    }

    public void checkRep() {
        int rowIndex = 0;
        int colIndex = 0;
        int[] subRegion = new int[size];

        // check that numbers are unique within rows
        while (rowIndex < size) {
            int[] row = new int[size];
            row = getRow(square, rowIndex);
            assertUnique(row);
            rowIndex++;
        }

        // check that numbers are unique within columns
        while (colIndex < size) {
            int[] col = new int[size];
            col = getColumn(square, colIndex);
            assertUnique(col);
            colIndex++;
        }

        // check that numbers are unique within regions
        colIndex = 0;
        rowIndex = 0;
        while (colIndex < size) {
            while(rowIndex < size) {
                subRegion = getRegion(square, dim, colIndex, rowIndex);
                assertUnique(subRegion);
                rowIndex = rowIndex + dim;
            }
            rowIndex = 0;
            colIndex = colIndex + dim;
        }
    }

    /**
     * create an empty Sudoku puzzle of dimension dim.
     * 
     * @param dim
     *            size of one block of the puzzle. For example, new Sudoku(3)
     *            makes a standard Sudoku puzzle with a 9x9 grid.
     */
    public Sudoku(int dim) {
        int size = dim*dim;
        int[][] square = new int[size][size];
        int col = 0;
        int row = 0;
        while (row < size) {
            while (col < size) {
                square[row][col] = -1;
                col++;
            }
            col = 0;
            row++;
        }
        this.square = square;
        this.dim = dim;
        this.size = size;
        checkRep();
        occupies = new Variable[size][size][size];
    }

    /**
     * create Sudoku puzzle
     * 
     * @param square
     *            digits or blanks of the Sudoku grid. square[i][j] represents
     *            the square in the ith row and jth column, contains 0 for a
     *            blank, else i to represent the digit i. So { { 0, 0, 0, 1 }, {
     *            2, 3, 0, 4 }, { 0, 0, 0, 3 }, { 4, 1, 0, 2 } } represents the
     *            dimension-2 Sudoku grid: 
     *            
     *            ...1 
     *            23.4 
     *            ...3
     *            41.2
     * 
     * @param dim
     *            dimension of puzzle Requires that dim*dim == square.length ==
     *            square[i].length for 0<=i<dim.
     */
    public Sudoku(int dim, int[][] square) {
        // TODO: implement this.
        int size = dim*dim;
        // assert that columns are correct length
        assert (size == square.length);
        int colIndex = 0;
        int rowIndex = 0;
        // assert to rows are correct length
        while (rowIndex < size) {
            assert (size == square[rowIndex].length);
            rowIndex++;
        }
        rowIndex = 0;
        while (rowIndex < size) {
            while (colIndex < size) {
                square[rowIndex][colIndex] = square[rowIndex][colIndex];
                colIndex++;
            }
            colIndex = 0;
            rowIndex++;
        }
        this.square = square;
        this.dim = dim;
        this.size = size;
        checkRep();
        // populate occupies
        int row = 0;
        int col = 0;
        int entry = 0;
        Variable entryVariable;
        occupies = new Variable[size][size][size];
        while (row < size) {
            while (col < size) {
                if (square[row][col] != -1) {
                    entry = square[row][col];
                    entryVariable = makeVariable(col, row, entry);
                    occupies[row][col][entry] = entryVariable;
                }
                col++;
            }
            col = 0;
            row++;
        }
    }

    /**
     * Reads in a file containing a Sudoku puzzle.
     * 
     * @param dim
     *            Dimension of puzzle. Requires: at most dim of 3, because
     *            otherwise need different file format
     * @param filename
     *            of file containing puzzle. The file should contain one line
     *            per row, with each square in the row represented by a digit,
     *            if known, and a period otherwise. With dimension dim, the file
     *            should contain dim*dim rows, and each row should contain
     *            dim*dim characters.
     * @return Sudoku object corresponding to file contents
     * @throws IOException
     *             if file reading encounters an error
     * @throws ParseException
     *             if file has error in its format
     */

     public static Sudoku fromFile(int dim, String filename) {
         int size = dim * dim;
         Path path = Paths.get(filename);
         List<String> tableString;
         int[][] table = new int[size][size];
         int rowIndex = 0;
         int colIndex = 0;
         char tableChar;
         int tableInt;

         try {
             tableString = Files.readAllLines(path);
             while (rowIndex < size) {
                 while (colIndex < size) {
                     tableChar = tableString.get(rowIndex).charAt(colIndex);
                     if (tableChar == '.') {
                         tableInt = -1;
                     } else tableInt = Character.getNumericValue(tableChar) - 1;
                     table[rowIndex][colIndex] = tableInt;
                     colIndex++;
                 }
                 colIndex = 0;
                 rowIndex++;
             }
         } catch (IOException ie) {
             ie.printStackTrace();
         }
         return new Sudoku(dim, table);
     }

    /**
     * Exception used for signaling grammatical errors in Sudoku puzzle files
     */
    @SuppressWarnings("serial")
    public static class ParseException extends Exception {
        public ParseException(String msg) {
            super(msg);
        }
    }

    /**
     * Produce readable string representation of this Sudoku grid, e.g. for a 4
     * x 4 sudoku problem: 
     *   12.4 
     *   3412 
     *   2.43 
     *   4321
     * 
     * @return a string corresponding to this grid
     */
    public String toString() {
        // TODO: implement this.
        final StringBuilder sb = new StringBuilder();
        int col = 0;
        int row = 0;
        int val;
        String valString;
        String eol = "\n";
        while (row < size) {
            while (col < size) {
                val = square[row][col];
                if (val == -1) {
                    valString = ".";
                }
                else valString = String.valueOf(square[row][col] + 1);
                sb.append(valString);
                col++;
            }
            if (row < size - 1) sb.append(eol);
            col = 0;
            row++;
        }
        return sb.toString();
    }

    private Variable makeVariable (int col, int row, int entry) {
        String string = "" + col + " " + row + " " + entry;
        Variable variable = new Variable(string);
        return variable;
    }

    /**
     * @return a SAT problem corresponding to the puzzle, using variables with
     *         names of the form occupies(i,j,k) to indicate that the kth symbol
     *         occupies the entry in row i, column j
     */
    public Formula getProblem() {

        // TODO: implement this.
        Formula formula = new Formula();
        int row = 0;
        int col = 0;
        int entry = 0;
        // add occupy variables to formula as separate ANDED clauses
        // and guarantee that all other values are excluded from occupied cells
        while (row < size) {
            while (col < size) {
                while (entry < size) {
                    Variable v = occupies[row][col][entry];
                    if (v != null) {
                        PosLiteral l = PosLiteral.make(v.getName());
                        formula = formula.addClause(new Clause(l));
                    }
                    entry++;
                }
                entry = 0;
                col++;
            }
            entry = 0;
            col = 0;
            row++;
        }

        // ensure that at most one digit can occupy each cell
        row = 0;
        col = 0;
        entry = 0;
        int entry2 = 0;

        while (row < size) {
            while (col < size) {
                while (entry < size) {
                    Variable v = makeVariable(col, row, entry);
                    NegLiteral nl = NegLiteral.make(v.getName());
                    while (entry2 < size) {
                        if (entry2 > entry) {
                            Variable v2 = makeVariable(col, row, entry2);
                            NegLiteral nl2 = NegLiteral.make(v2.getName());
                            Clause c = new Clause(nl);
                            c = c.add(nl2);
                            formula = formula.addClause(c);
                        }
                        entry2++;
                    }
                    entry2 = 0;
                    entry++;
                }
                entry = 0;
                col++;
            }
            col = 0;
            row++;
        }
//
//        // ensure that each digit occurs in each row at least once
        row = 0;
        col = 0;
        entry = 0;

        while (row < size) {
            while (entry < size) {
                Clause c = new Clause();
                while (col < size) {
                    Variable v = makeVariable(col, row, entry);
                    PosLiteral l = PosLiteral.make(v.getName());
                    c = c.add(l);
                    col++;
                }
                formula = formula.addClause(c);
                col = 0;
                entry++;
            }
            entry = 0;
            row++;
        }

        // ensure that each digit occurs at most once in each row
        row = 0;
        int col2 = 0;
        col = 0;
        entry = 0;
        while (entry < size) {
            while (row < size) {
                while (col < size) {
                    Variable v = makeVariable(col, row, entry);
                    NegLiteral l = NegLiteral.make(v.getName());
                    while (col2 < size) {
                        if (col < col2) {
                            Variable v2 = makeVariable(col2, row, entry);
                            NegLiteral l2 = NegLiteral.make(v2.getName());
                            Clause c = new Clause();
                            c = c.add(l);
                            c = c.add(l2);
                            formula = formula.addClause(c);
                        }
                        col2++;
                    }
                    col2 = 0;
                    col++;
                }
                col = 0;
                row++;
            }
            row = 0;
            entry++;
        }


        // ensure that each digit occurs in each column at least once
        row = 0;
        col = 0;
        entry = 0;

        while (col < size) {
            while (entry < size) {
                Clause c = new Clause();
                while (row < size) {
                    Variable v = makeVariable(col, row, entry);
                    PosLiteral l = PosLiteral.make(v.getName());
                    c = c.add(l);
                    row++;
                }
                row = 0;
                entry++;
            }
            entry = 0;
            col++;
        }

        // ensure that each digit occurs at most once in each column
        row = 0;
        int row2 = 0;
        col = 0;
        entry = 0;
        while (entry < size) {
            while (col < size) {
                while (row < size) {
                    Variable v = makeVariable(col, row, entry);
                    NegLiteral l = NegLiteral.make(v.getName());
                    while (row2 < size) {
                        if (row < row2) {
                            Clause c = new Clause(l);
                            Variable v2 = makeVariable(col, row2, entry);
                            NegLiteral l2 = NegLiteral.make(v2.getName());
                            c = c.add(l);
                            c = c.add(l2);
                            formula = formula.addClause(c);
                        }
                        row2++;
                    }
                    row2 = 0;
                    row++;
                }
                row = 0;
                col++;
            }
            col = 0;
            entry++;
        }


        // ensure that each entry occurs at least once in each region

        entry = 0;
        int regionRowBegin = 0;
        int regionColBegin = 0;
        row = regionRowBegin;
        col = regionColBegin;

        while (regionRowBegin + dim < size) {
            while (regionColBegin + dim < size) {
                while (entry < size) {
                    Clause c = new Clause();
                    while (row < regionRowBegin + dim) {
                        while (col < regionColBegin + dim) {
                            Variable v = makeVariable(col, row, entry);
                            PosLiteral l = PosLiteral.make(v.getName());
                            c = c.add(l);
                            col++;
                        }
                        col = regionColBegin;
                        row++;
                    }
                    formula = formula.addClause(c);
                    row = regionRowBegin;
                    entry++;
                }
                regionColBegin = regionColBegin + dim;
                col = regionColBegin;
                entry = 0;
            }
            regionColBegin = 0;
            col = regionColBegin;
            regionRowBegin = regionRowBegin + dim;
            row = regionRowBegin;
        }

        // ensure that each entry occurs at most once in each region

        entry = 0;
        regionRowBegin = 0;
        regionColBegin = 0;
        row = regionRowBegin;
        col = regionColBegin;
        row2 = regionRowBegin;
        col2 = regionColBegin;

        while (regionRowBegin + dim < size) {
            while (regionColBegin + dim < size) {
                while (entry < size) {
                    while (row < regionRowBegin + dim) {
                        while (col < regionColBegin + dim) {
                            Variable v = makeVariable(col, row, entry);
                            NegLiteral l = NegLiteral.make(v.getName());
                            while (row2 < regionRowBegin + dim) {
                                while (col2 < regionColBegin + dim) {
                                    if (((row2 == row) && (col2 > col)) || row2 > row) {
                                        Clause c = new Clause(l);
                                        Variable v2 = makeVariable(col2, row2, entry);
                                        NegLiteral l2 = NegLiteral.make(v2.getName());
                                        c = c.add(l2);
                                        formula = formula.addClause(c);
                                    }
                                    col2++;
                                }
                                col2 = regionColBegin;
                                row2++;
                            }
                            row2 = regionRowBegin;
                            col++;
                        }
                        col = regionColBegin;
                        row++;
                    }
                    row = regionRowBegin;
                    entry++;
                }
                regionColBegin = regionColBegin + dim;
                col = regionColBegin;
                entry = 0;
            }
            regionColBegin = 0;
            col = regionColBegin;
            regionRowBegin = regionRowBegin + dim;
            row = regionRowBegin;
        }



        return formula;
    }

    /**
     * Interpret the solved SAT problem as a filled-in grid.
     * 
     * @param e
     *            Assignment of variables to values that solves this puzzle.
     *            Requires that e came from a solution to this.getProblem().
     * @return a new Sudoku grid containing the solution to the puzzle, with no
     *         blank entries.
     */
    public Sudoku interpretSolution(Environment e) {
        int[][] square = new int[size][size];
        // get variables bound to true
        int row = 0;
        int col = 0;
        int entry = 0;
        while (row < size) {
            while (col < size) {
                while (entry < size) {
                    Variable v = makeVariable(col, row, entry);
                    Bool b = e.get(v);
                    if (b == Bool.TRUE) {
                        square[row][col] = entry;
                    }
                    entry++;
                }
                entry = 0;
                col++;
            }
            col = 0;
            row++;
        }
        return new Sudoku(dim, square);
    }

}
