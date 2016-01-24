package minesweeper;

import java.util.Arrays;

public class Board {

    int dim;
    private final BoardStates bombs;
    private final BoardStates flag;
    private final BoardStates touched;
    private final BoardStates borderBombs;
    private final BoardStates noBorderBombs;

    private static char[][] createBoard (int n) {
        char[][] board = new char[n][n];
        for (char[] row : board) {
            Arrays.fill(row, '-');
        }
        return board;
    }

    public Board (int n) {
        this.dim = n;
        bombs = new BoardStates(n, 0.1);
        flag = new BoardStates(n);
        touched = new BoardStates(n);
        borderBombs = new BoardStates(n);
        noBorderBombs = new BoardStates(n);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (flag.getState(i,j) == 1) {
                    string.append("F");
                } else if (touched.getState(i,j) == 0) {
                    string.append("-");
                } else if (borderBombs.getState(i,j) > 0) {
                    string.append(borderBombs.getState(i,j));
                } else {
                    string.append(" ");
                }
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }


    public String dig(int row, int col) {
        String printBoard = "Print board";
        // if row, col is not on board, just print board
        if (row < 0 || col < 0 || row >= dim || col >= dim) {
            return printBoard;
            // else if square is already dug, just print board
        } else if (touched.getState(row, col) == 1 || flag.getState(row, col) == 1) {
            return printBoard;
            // else if square contains bomb, then remove
            // bomb from board, print BOOM!, and disconnect user
        } else if (bombs.getState(row, col) == 1) {
            bombs.changeState(row, col);
            touched.setState(row, col, 0);
            return "BOOM!";
            // otherwise, square does not contain bomb
        } else {
            // count number of bombs in bordering squares
            int borderBomb = bombs.boarderSum(row,col);
            // if there are no bombs, change current square to dug
            // set current square to noBorderBombs, and run dig
            // on all bordering squares
            if (borderBomb == 0) {
                touched.setState(row, col, 1);
                noBorderBombs.setState(row, col, 1);
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        if (BoardStates.borders(row, col, i, j)) {
                            dig(i, j);
                        }
                    }
                }
                // else, record number of bordering squares with bombs
                // and set square to dug
            } else {
                borderBombs.setState(row, col, borderBomb);
                touched.setState(row, col, 1);
            }
        }
        return printBoard;
    }

    public String flag(int row, int col) {
        String printBoard = "Print board";
        // if row, col is not on board, just print board
        if (row < 0 || col < 0 || row >= dim || col >= dim || touched.getState(row, col) == 1) {
            return printBoard;
        } else {
            flag.setState(row, col, 1);
        }
        return printBoard;
    }

    public String unflag(int row, int col) {
        String printBoard = "Print board";
        // if row, col is not on board, just print board
        if (flag.getState(row, col) == 1) {
            flag.changeState(row, col);
        }
        return printBoard;
    }

    public static void main(String[] args) {
        Board board = new Board(5);
        System.out.print(board.toString());
        System.out.print(board.bombs.toString());
        board.dig(1,1);
        System.out.print(board.toString());
        System.out.print(board.bombs.toString());
    }
}
