package parser;

import sound.Pitch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by clay on 12/14/15.
 */
public class Rest implements NoteInterface {

    private String restString;
    private int numerator = 1;
    private int denominator = 1;
    private String pitch;

    public String getRestString() {return this.restString;}
    public double getLength() {return (double)numerator/(double)denominator;}

    public Rest (String noteString, Signature signature) {
        // get length
        Pattern justNumber = Pattern.compile("z[0-9](?!/)");
        Matcher mJustNumber = justNumber.matcher(noteString);
        Boolean mJustNumberFound = mJustNumber.find();
        Pattern pLength = Pattern.compile("[0-9]*/[0-9]*|/");
        Matcher m = pLength.matcher(noteString);
        Boolean found = m.find();
        int start;
        int end;
        if (mJustNumberFound) {
            mJustNumber.group();
            end = mJustNumber.end();
            numerator = Integer.parseInt(noteString.substring(end-1, end));
            denominator = 1;
        }
        if (found) {
            m.group();
            start = m.start();
            end = m.end();
            if (end - start == 1) {
                this.numerator = 1;
                this.denominator = 2;
            } else if (noteString.substring(start, start+1).equals("/")) {
                this.numerator = 1;
                this.denominator = Integer.parseInt(noteString.substring(end-1, end));
            } else if (noteString.substring(end-1, end).equals("/")) {
                this.numerator = Integer.parseInt(noteString.substring(start, start+1));
                this.denominator = 2;
            } else {
                this.numerator = Integer.parseInt(noteString.substring(start, start+1));
                this.denominator = Integer.parseInt(noteString.substring(end-1, end));
            }
        }
    }

    public String toString() {
        return "Rest: " + numerator + "/" + denominator;
    }
    public String getPitch() {return null;}
    public <R> R accept(Visitor<R> v) {return v.on(this);}
}
