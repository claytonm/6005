package sat;

import immutable.ImList;
import immutable.EmptyImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.SetValue;
import sat.env.Bool;
import java.util.Iterator;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        ImList<Clause> clauses = formula.getClauses();
        Environment env = new Environment();
        return solve(clauses, env);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // if there are no clauses, the formula is trivially satistfiable
        if (clauses.isEmpty()) return env;
        // if there is an empty clause, the formula is not satisfiable
        for (Clause clause: clauses) {
            if (clause.isEmpty()) return null;
        }
        // find smallest clause
        double smallestSize = Double.POSITIVE_INFINITY;
        int clauseSize;
        Clause smallestClause = new Clause();
        for (Clause clause: clauses) {
            clauseSize = clause.size();
            if (clauseSize < smallestSize) {
                smallestClause = clause;
                smallestSize = clauseSize;
            }
        }

        Environment returnEnv = new Environment();
        Environment newEnvironment = new Environment();
        ImList<Clause> newClauses = new EmptyImList();
        Literal literal = smallestClause.chooseLiteral();

        newEnvironment = literal.setValue(literal, env);
        newClauses = substitute(clauses, literal);

        if (smallestClause.isUnit()) {
            returnEnv = solve(newClauses, newEnvironment);
        } else {
            returnEnv = solve(newClauses, newEnvironment);
            if (null == returnEnv) {
                Literal negation = literal.getNegation();
                newEnvironment = negation.setValue(negation, env);
                newClauses = substitute(clauses, negation);
                returnEnv = solve(newClauses, newEnvironment);
            }
        }
        return returnEnv;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        ImList<Clause> newClauses = new EmptyImList();
        if (clauses.isEmpty()) {return newClauses;}
        Clause newClause = new Clause();
        for (Clause clause: clauses) {
            if (!clause.isEmpty() && !(clause == null)) {newClause = clause.reduce(l);}
            if (!(newClause == null)) {
                newClauses = newClauses.add(newClause);
            }
        }
        return newClauses;
    }
}
