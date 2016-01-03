package parser;
//
///**
// * Created by clay on 12/3/15.
// */
//

import com.sun.deploy.security.ValidationState;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//
///**
// * Musical lexical analyzer.
// */
public class Lexer {
    //
    int i;
    int N;
    final String input;
//

    /**
     * Token in the stream.
     */
    public static class Token {
        final Type type;
        final String text;

        Token(Type type, String text) {
            this.type = type;
            this.text = text;
        }

        Token(Type type) {
            this(type, null);
        }

        public String toString() {
            return "Token: " + this.getText() + " " + this.type.toString() + "\n";
        }

        public Type getType() {
            return this.type;
        }

        public String getText() {
            return this.text;
        }

        public boolean isBar() {
            return this.getType() == Type.BAR;
        }

        public boolean isDoubleBar() {
            return this.getType() == Type.DOUBLEBAR;
        }

        public boolean isNote() {
            return this.getType() == Type.NOTE;
        }

        public boolean isOpenRepeat() {
            return this.getType() == Type.OPENREPEAT;
        }

        public boolean isCloseRepeat() {
            return this.getType() == Type.CLOSEREPEAT;
        }

        public boolean isRest() { return this.getType() == Type.REST;}

        public boolean isChord() { return this.getType() == Type.CHORD;}

        public boolean isTuple() { return this.getType() == Type.TUPLET;}

    }

    @SuppressWarnings("serial")
    static class TokenMismatchException extends Exception {
    }

    // TODO write method spec
    public Lexer(String input) {
        i = 0;
        String inputTemp = input;
        this.input = inputTemp;
        this.input.replaceAll("\\s+", "");
        N = this.input.length();
    }

    public boolean hasNext() {
        return i < N;
    }


    public boolean isVoice(String s) {
        Pattern p = Pattern.compile("^V:[0-9{1}]");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isNote(String s) {
        Pattern p = Pattern.compile("^(\\^{1,2}|_{1,2}|\\=)?[a-gA-G](\\'|\\,)*([0-9]*/[0-9]*|/|[0-9])?");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isRest(String s) {
        Pattern p = Pattern.compile("^z([0-9]*/[0-9]*|/|[0-9])?");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isBar(String s) {
        Pattern p = Pattern.compile("^\\|{1}(?!\\])");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isTuple(String s) {
        Pattern p = Pattern.compile("^\\([2-4]((\\^{1,2}|\\_{1,2}|\\=)?[a-gA-G](\\'|\\,)*){2,4}");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isChord(String s) {
        Pattern p = Pattern.compile("^\\[((\\^{1,2}|_{1,2}|\\=)?[a-gA-G](\\'|\\,)*([0-9]*/[0-9]*|/|[0-9])?)+\\]");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isDoubleBar(String s) {
        Pattern p = Pattern.compile("^\\|{2}|(\\|\\]){1}");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isOpenRepeat(String s) {
        Pattern p = Pattern.compile("^(\\|\\:)");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isNthRepeat(String s) {
        Pattern p = Pattern.compile("^(\\[[1-2]{1})");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isCloseRepeat(String s) {
        Pattern p = Pattern.compile("^(\\:\\|)");
        Matcher m = p.matcher(s);
        return (m.find());
    }


    public int getBlankSpace(String s) {
        Pattern p = Pattern.compile("^\\s+");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getNote(String s) {
        Pattern p = Pattern.compile("^(\\^{1,2}|\\_{1,2}|\\=)?[a-gA-G](\\'|\\,)*([0-9]*/[0-9]*|/|[0-9])?");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getRest(String s) {
        Pattern p = Pattern.compile("^z([0-9]*/[0-9]*|/|[0-9])?");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getVoice(String s) {
        Pattern p = Pattern.compile("^V:[0-9]{1}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getBar(String s) {
        Pattern p = Pattern.compile("^\\|{1}(?!\\])");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getDoubleBar(String s) {
        Pattern p = Pattern.compile("^\\|{2}|(\\|\\]){1}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getNthRepeat(String s) {
        Pattern p = Pattern.compile("^(\\[[1-2])");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getOpenRepeat(String s) {
        Pattern p = Pattern.compile("^(\\|\\:)");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getCloseRepeat(String s) {
        Pattern p = Pattern.compile("^(\\:\\|)");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getTuple(String s) {
        Pattern p = Pattern.compile("^\\([2-4]((\\^{1,2}|\\_{1,2}|\\=)?[a-gA-G](\\'|\\,)*){2,4}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public int getChord(String s) {
        Pattern p = Pattern.compile("^\\[((\\^{1,2}|_{1,2}|\\=)?[a-gA-G](\\'|\\,)*([0-9]*/[0-9]*|/|[0-9])?)+\\]");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public Token next() {
        if (hasNext()) {
            if (isNote(input.substring(i, N))) {
                int endMatch = getNote(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.NOTE, input.substring(j, i)));
            } else if (isChord(input.substring(i, N))) {
                int endMatch = getChord(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.CHORD, input.substring(j, i)));
            } else if (isRest(input.substring(i, N))) {
                int endMatch = getRest(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.REST, input.substring(j, i)));
            } else if (isVoice(input.substring(i, N))) {
                int endMatch = getVoice(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.VOICE, input.substring(j, i)));
            } else if (isOpenRepeat(input.substring(i, N))) {
                int endMatch = getOpenRepeat(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.OPENREPEAT, input.substring(j, i)));
            } else if (isNthRepeat(input.substring(i, N))) {
                int endMatch = getNthRepeat(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.NTHREPEAT, input.substring(j, i)));
            } else if (isBar(input.substring(i, N))) {
                int endMatch = getBar(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.BAR, input.substring(j, i)));
            } else if (isTuple(input.substring(i, N))) {
                int tupletNum = Integer.valueOf(input.substring(i+1,i+2));
                StringBuilder tupletString = new StringBuilder();
                int n = 0;
                i = i+2;
                while (n < tupletNum) {
                    int endMatch = getNote(input.substring(i, N));
                    int j = i;
                    i = i + endMatch;
                    tupletString.append(input.substring(j,i));
                    n++;
                }
                return (new Token(Type.TUPLET, tupletString.toString()));
            } else if (isCloseRepeat(input.substring(i, N))) {
                int endMatch = getCloseRepeat(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.CLOSEREPEAT, input.substring(j, i)));
            } else if (isDoubleBar(input.substring(i, N))) {
                int endMatch = getDoubleBar(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.DOUBLEBAR, input.substring(j, i)));
            }
                else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void main (String[]  args) {
        Lexer lexer = new Lexer("ABC(2D'_EABC");
        while (lexer.hasNext()) {
            System.out.print(lexer.next());
        }
    }
}
