package sat.formula;

import static org.junit.Assert.*;
import org.junit.Test;
import sat.env.Variable;

public class FormulaTest {    
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
    public void testFormulaVariable() {
        Formula formula = new Formula(a);
        System.out.println(formula.toString());
    }

    @Test
    public void testFormulaNegatedVariable() {
        Formula formula = new Formula(na);
        // System.out.println(formula.toString());
    }

    @Test
    public void testFormulaAnd() {
        String string = "" + 1 + " " + 2 + " " + 3;
        System.out.print(string);
        Formula formula = new Formula();
        Formula formulaA = new Formula(a);
        Formula formulaB = new Formula(b);
        Formula formulaC = new Formula(c);
        Formula formulaNA = new Formula(na);
        Formula formulaAandB = formulaA.and(formulaB);
        Formula formulaAandBandC = formulaA.and(formulaB).and(formulaNA.or(formulaC));
        Formula formulaOr = formulaAandB.or(formulaAandBandC);
        Formula formulaAndBNot = formulaAandBandC.not();
        System.out.println(formula.toString());
        System.out.println(formulaA.toString());
        System.out.println(formulaAandBandC.toString());
        // System.out.println(formulaAandNA.toString());
        System.out.println(formulaOr.toString());
        System.out.println(formulaAndBNot.toString());
    }
}