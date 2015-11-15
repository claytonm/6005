package sudoku;


import static org.junit.Assert.*;
import org.junit.Test;
import sat.env.Environment;
import sat.formula.Formula;
import sat.SATSolver;

public class SudokuTest {
    

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test // (expected=AssertionError.class)
    public void testAssertionsEnabled() {
        int val1 = 5;
        int val2 = 5;
        assertEquals(val1, val2);
    }

    @Test
    public void testSudkuValidTable() {
        // should run without error
        Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 0, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 2, 0, 3, 0 },
                new int[] { 0, 0, 0, 0 },
        });

        System.out.println(testTable.toString());
    }

    @Test
    public void testSudkuValidEmptyTable() {
        // should run without error
        Sudoku testTable = new Sudoku(3);
        System.out.println(testTable.toString());
    }

    @Test
    public void testSudokuInvalidColumn() {
        // should throw assertUnique exception
        Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 3, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 2, 0, 3, 0 },
                new int[] { 0, 0, 0, 0 },
        });
    }

    @Test
    public void testSudokuInvalidRow() {
        // should throw assertUnique exception
        Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 0, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 3, 0, 3, 0 },
                new int[] { 0, 0, 0, 0 },
        });

    }

    @Test
    public void testSudokuIvalidRegion() {
        // should throw assertUnique exception
        Sudoku testTable = new Sudoku(2, new int[][] {
                new int[] { 0, 1, 0, 4 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 0, 0, 3, 0 },
                new int[] { 0, 0, 0, 3 },
        });
    }

    @Test
    public void readSquareFromFile() {
        String filePath = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/ps4/samples/sudoku_easy.txt";
        Sudoku table = Sudoku.fromFile(3, filePath);
        System.out.println(table.toString());
        String tableCorrect = "2..1.5..3\n.54...71.\n.1.2.3.8.\n6.28.73.4\n.........\n1.53.98.6\n.2.7.1.6.\n.81...24.\n7..4.2..1";
        assertEquals(table.toString(), tableCorrect);
    }

    @Test
    public void solveEasy2() {
        String filePath = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/ps4/samples/sudoku_easy2.txt";
        Sudoku table = Sudoku.fromFile(3, filePath);
//        System.out.print(table.toString());
        Formula formula = table.getProblem();
        Environment env = SATSolver.solve(formula);
        Sudoku sudokuSolved = new Sudoku(3);
        sudokuSolved = sudokuSolved.interpretSolution(env);
        System.out.print(sudokuSolved.toString());
    }

    @Test
    public void solveEasy() {
        String filePath = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/ps4/samples/sudoku_easy.txt";
        Sudoku table = Sudoku.fromFile(3, filePath);
        Formula formula = table.getProblem();
        Environment env = SATSolver.solve(formula);
        System.out.print(env.toString());
    }

    @Test
    public void solveHard() {
        String filePath = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/ps4/samples/sudoku_hard.txt";
        Sudoku table = Sudoku.fromFile(3, filePath);
        Formula formula = table.getProblem();
        Environment env = SATSolver.solve(formula);
        System.out.print(env.toString());
    }

    @Test
    public void solveHard2() {
        String filePath = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/ps4/samples/sudoku_hard2.txt";
        Sudoku table = Sudoku.fromFile(3, filePath);
        Formula formula = table.getProblem();
        Environment env = SATSolver.solve(formula);
        System.out.print(env.toString());
    }

    @Test
    public void solveEvil() {
        String filePath = "/Users/clay/education/6-005-fall-2011/6-005-fall-2011/contents/assignments/source_files/ps4/samples/sudoku_evil.txt";
        Sudoku table = Sudoku.fromFile(3, filePath);
        Formula formula = table.getProblem();
        Environment env = SATSolver.solve(formula);
        System.out.print(env.toString());
    }



}
