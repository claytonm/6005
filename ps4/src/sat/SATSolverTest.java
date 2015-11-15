package sat;

import static org.junit.Assert.*;

import org.junit.Test;

import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testSolverEasyCase() {
        Formula formula = new Formula(na);
        Environment env;
        env = SATSolver.solve(formula);
        System.out.println(env.toString());
    }
    @Test
    public void testSolverEasyCaseAND() {
        Formula fNA = new Formula(na);
        Formula fA = new Formula(a);
        Formula fAND = fA.and(fNA);
        Environment env;
        env = SATSolver.solve(fAND);
        System.out.println(env.toString());
    }
    @Test
    public void testSolver1() {
        Formula fA = new Formula(a);
        Formula fB = new Formula(b);
        Formula fNB = new Formula(nb);
        Formula f = (fA.or(fNB)).and(fA.or(fB));
        Environment env;
        env = SATSolver.solve(f);
        System.out.println(env.toString());
    }
    @Test
    public void testSolver2() {
        Formula fA = new Formula(a);
        Formula fB = new Formula(b);
        Formula fNB = new Formula(nb);
        Formula f = (fA.and(fB)).and(fA.and(fNB));
        Environment env;
        env = SATSolver.solve(f);
        System.out.println(env.toString());
    }
    @Test
    public void testSolver3() {
        Formula fA = new Formula(a);
        Formula fB = new Formula(b);
        Formula fC = new Formula(c);
        Formula fNB = new Formula(nb);
        Formula f = (fA.and(fB)).and(fNB.or(fC));
        Environment env;
        env = SATSolver.solve(f);
        System.out.println(env.toString());
    }
}