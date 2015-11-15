package sat.formula;

import javafx.geometry.Pos;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;

/**
 * Created by clay on 11/11/15.
 */
public class SetValue implements Visitor<Environment>{
    public Environment on(PosLiteral l, Environment env) {
        return env.putTrue(l.getVariable());
    }
    public Environment on(NegLiteral l, Environment env) {
        return env.putFalse(l.getVariable());
    }
}
