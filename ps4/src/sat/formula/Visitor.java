package sat.formula;

import sat.env.Environment;

/**
 * Created by clay on 11/11/15.
 */
public interface Visitor<R> {
    // public R on(Literal l, Environment env);
    public R on(NegLiteral n, Environment env);
    public R on(PosLiteral p, Environment env);
}
