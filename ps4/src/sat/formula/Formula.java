/**
 * Author: dnj, Hank Huang
 * Date: March 7, 2009
 * 6.005 Elements of Software Construction
 * (c) 2007-2009, MIT 6.005 Staff
 */
package sat.formula;

import immutable.ImList;
import immutable.EmptyImList;
import immutable.ImListIterator;

import java.awt.*;
import java.util.Iterator;

import sat.env.Variable;

/**
 * Formula represents an immutable boolean formula in
 * conjunctive normal form, intended to be solved by a
 * SAT solver.
 * Datatype definiting (per Lecture 7):
 * Formula = ImList<Clause>
 * Clause = ImList<Literal>
 * Literal = Positive(v: Var) + Negative(v:Var)
 * Var = String
 */
public class Formula {
    private final ImList<Clause> clauses;
    // Rep invariant:
    //      clauses != null
    //      clauses contains no null elements (ensured by spec of ImList)
    //
    // Note: although a formula is intended to be a set,  
    // the list may include duplicate clauses without any problems. 
    // The cost of ensuring that the list has no duplicates is not worth paying.
    //
    //    
    //    Abstraction function:
    //        The list of clauses c1,c2,...,cn represents 
    //        the boolean formula (c1 and c2 and ... and cn)
    //        
    //        For example, if the list contains the two clauses (a,b) and (!c,d), then the
    //        corresponding formula is (a or b) and (!c or d).

    void checkRep() {
        assert this.clauses != null : "SATProblem, Rep invariant: clauses non-null";
    }

    /**
     * Create a new problem for solving that contains no clauses (that is the
     * vacuously true problem)
     * 
     * @return the true problem
     */
    public Formula() {
        this.clauses = new EmptyImList<Clause>();
    }

    /**
     * Create a new problem for solving that contains a single clause with a
     * single literal
     * 
     * @return the problem with a single clause containing the literal l
     */
    public Formula(Literal l) {
        EmptyImList emptyClauses = new EmptyImList<Clause>();
        Clause clause = new Clause(l);
        this.clauses = emptyClauses.add(clause);
        checkRep();
    }

    /**
     * Create a new problem for solving that contains a single clause
     * 
     * @return the problem with a single clause c
     */
    public Formula(Clause c) {
        EmptyImList emptyClauses = new EmptyImList<Clause>();
        this.clauses = emptyClauses.add(c);
        checkRep();
    }

    /** Create a new problem with clauses
     *
     */


    public Formula(ImList<Clause> clauses) {
        this.clauses = clauses;
        checkRep();
    }

    /**
     * Add a clause to this problem
     * 
     * @return a new problem with the clauses of this, but c added
     */
    public Formula addClause(Clause c) {
        ImList<Clause> clauses = this.getClauses().add(c);
        Formula formula = new Formula(clauses);
        return formula;
    }

    /**
     * Get the clauses of the formula.
     * 
     * @return list of clauses
     */
    public ImList<Clause> getClauses() {
        return this.clauses;
    }

    /**
     * Iterator over clauses
     * 
     * @return an iterator that yields each clause of this in some arbitrary
     *         order
     */
    public Iterator<Clause> iterator() {
        return new ImListIterator(this.clauses);
    }

    /**
     * @return a new problem corresponding to the conjunction of this and p
     */
    public Formula and(Formula p) {
        Formula formula = this;
        Iterator<Clause> pIter = p.iterator();
        while (pIter.hasNext()) {
            formula = formula.addClause(pIter.next());
        }
        return formula;
    }

    /**
     * @return a new problem corresponding to the disjunction of this and p
     */
    public Formula or(Formula p) {
        // Hint: you'll need to use the distributive law to preserve conjunctive normal form, i.e.:
        //   to do (a & b) .or (c & d),
        //   you'll need to make (a | b) & (a | c) & (b | c) & (b | d)
        ImList<Clause> clauses = new EmptyImList<Clause>();
        Iterator<Clause> thisIter = this.iterator();
        Iterator<Clause> pIter = p.iterator();
        while (thisIter.hasNext()) {
            Clause next = thisIter.next();
            while (pIter.hasNext()) {
                Clause nextP = pIter.next();
                if (null != next.merge(nextP)) {
                    clauses = clauses.add(next.merge(nextP));
                }
            }
            pIter = p.iterator();
        }
        Formula formula = new Formula(clauses);
        return formula;
    }

    /**
     * @return a new problem corresponding to the negation of this
     */
    public Formula not() {
        // Hint: you'll need to apply DeMorgan's Laws (http://en.wikipedia.org/wiki/De_Morgan's_laws)
        // to move the negation down to the literals, and the distributive law to preserve 
        // conjunctive normal form, i.e.:
        //   if you start with (a | b) & c,
        //   you'll need to make !((a | b) & c) 
        //                       => (!a & !b) | !c            (moving negation down to the literals)
        //                       => (!a | !c) & (!b | !c)    (conjunctive normal form)
        // For each clause in formula, negate all literals in clause.
        // Then "or" the clause with the remainder of the formula
        Formula formula = new Formula();
        Iterator<Clause> thisIter = this.iterator();
        while (thisIter.hasNext()) {
            Clause nextClause = thisIter.next();
            Iterator<Literal> clauseIter = nextClause.iterator();
            Formula negateFormulaAnd = new Formula();
            while (clauseIter.hasNext()) {
                Formula negateFormula;
                Literal nextLiteral = clauseIter.next();
                Literal nextLiteralNegation = nextLiteral.getNegation();
                negateFormula = new Formula(nextLiteralNegation);
                negateFormulaAnd = negateFormulaAnd.and(negateFormula);
            }
            if (formula.getSize() == 0) {
                formula = negateFormulaAnd;
            } else formula = formula.or(negateFormulaAnd);
        }
        return formula;
    }

    /**
     * 
     * @return number of clauses in this
     */
    public int getSize() {
        return clauses.size();
    }

    /**
     * @return string representation of this formula
     */
    public String toString() {
        String result = "Problem[";
        for (Clause c : clauses)
            result += "\n" + c;
        return result + "]" + "\n";
    }
}
