package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by clay on 12/9/15.
 */
public class BarSignature {
    Map<String, Integer> barSignature = new HashMap<String, Integer>();

    public BarSignature() {
        barSignature.put("A", 0);
        barSignature.put("B", 0);
        barSignature.put("C", 0);
        barSignature.put("D", 0);
        barSignature.put("E", 0);
        barSignature.put("F", 0);
        barSignature.put("G", 0);
    }

    public void addAccidental(String string, int accidental) {
        barSignature.put(string, accidental);
    }

    public void addNeutral(String string) {
        barSignature.put(string, 0);
    }

    public int getAccidental(String pitch) {
        return this.barSignature.get(pitch);
    }
}
