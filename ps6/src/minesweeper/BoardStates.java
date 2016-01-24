package minesweeper;

/**
 * Created by clay on 1/18/16.
 */
public class BoardStates {
    int[][] states;
    int dim;

    public BoardStates (int n, double prob) {
        dim = n;
        int[][] states = new int[n][n];
        for (int[] row : states) {
            for (int i = 0; i < n; i++) {
                if (Math.random() < prob) {
                    row[i] = 1;
                } else {
                    row[i] = 0;
                }
            }
        }
        this.states = states;
    }

    public BoardStates (int n) {
        dim = n;
        int[][] states = new int[n][n];
        for (int[] row : states) {
            for (int i = 0; i < n; i++) {
                row[i] = 0;
            }
        }
        this.states = states;
    }


    public int getState(int row, int col) {
        return states[row][col];
    }

    public void changeState(int row, int col) {
        if (getState(row, col) == 1) {
            states[row][col] = 0;
        } else {
            states[row][col] = 1;
        }
    }

    public void setState(int row, int col, int state) {
        states[row][col] = state;
    }

    public static boolean borders(int row, int col, int otherRow, int otherCol) {
        return (Math.abs(row - otherRow) <= 1 && Math.abs(col - otherCol) <= 1 && (row != otherRow || col != otherCol));
    }

    public int boarderSum(int row, int col) {
        int sum = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (borders(row, col, i, j)) {
                    sum = sum + states[i][j];
                }
            }
        }
        return sum;
    }

    public void changeBorderStates(int row, int col) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (borders(row, col, i, j)) {
                    changeState(i, j);
                }
            }
        }
    }

    public void setBorderBombs(int row, int col, int bombs) {
        states[row][col] = bombs;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int[] row : states) {
            for (int i = 0; i < dim; i++) {
                string.append(row[i]);
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }
}
