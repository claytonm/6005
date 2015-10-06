package calculator;

import calculator.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import calculator.Parser.ValueType;
/**
 * Calculator lexical analyzer.
 */
public class Lexer {

    int i;
    int N;
    final String input;

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

        public double getTokenValue() {
            if (type == Type.SCALAR) {
                return (Double.parseDouble(text));
            } else {
                return (Double.parseDouble(text.substring(0, text.length() - 2)));
            }
        }

        public Type getOperatorType() {
            return type;
        }

        public ValueType getTokenType() {
            if (type == Type.SCALAR) {
                return (ValueType.SCALAR);
            } else if (type == Type.INCHES) {
                return (ValueType.INCHES);
            } else {
                return (ValueType.POINTS);
            }
        }
    }

    @SuppressWarnings("serial")
    static class TokenMismatchException extends Exception {
    }

    // TODO write method spec
    public Lexer(String input) {
        i = 0;
        String inputTemp = input;
        this.input = '(' + inputTemp + ')';
        this.input.replaceAll("\\s+","");
        N = this.input.length();
    }


    public boolean hasNext() {
        return i < N;
    }

    public static Token makeMultiply() {
        return (new Token(Type.MULTIPLY, "*"));
    }

    public static Token makePoint() {
        return (new Token(Type.POINTS, "1PT"));
    }

    public static Token makeInch() {
        return (new Token(Type.INCHES, "1IN"));
    }

    public boolean isPoint(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?PT");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isInch(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?IN");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isScalar(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?(?!(PT|IN))");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public Integer getPoint(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?(PT){1}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public Integer getInch(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?IN");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public boolean isInchSymbol(String s) {
        Pattern p = Pattern.compile("^IN{1}");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public Integer getInchSymbol(String s) {
        Pattern p = Pattern.compile("^IN{1}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public boolean isPointSymbol(String s) {
        Pattern p = Pattern.compile("^PT{1}");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public Integer getPointSymbol(String s) {
        Pattern p = Pattern.compile("^PT{1}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }


    public Integer getScalar(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?(?!(PT|IN))");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }


    public Token next() {
        if (hasNext()) {
            if (input.charAt(i) == '(') {
                i++;
                return (new Token(Type.OPENPAREN, "("));
            } else if (input.charAt(i) == ')') {
                i++;
                return (new Token(Type.CLOSEPAREN, ")"));
            } else if (input.charAt(i) == '+') {
                i++;
                return (new Token(Type.PLUS, "+"));
            } else if (input.charAt(i) == '-') {
                this.i++;
                return (new Token(Type.MINUS, "-"));
            } else if (input.charAt(i) == '*') {
                i++;
                return (new Token(Type.MULTIPLY, "*"));
            } else if (isInch(input.substring(i, N))) {
                int endMatch = getInch(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.INCHES, input.substring(j, i)));
            } else if (isPoint(input.substring(i, N))) {
                int endMatch = getPoint(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.POINTS, input.substring(j, i)));
            } else if (isScalar(input.substring(i, N))) {
                int endMatch = getScalar(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.SCALAR, input.substring(j, i)));
            } else if (isInchSymbol(input.substring(i, N))) {
                int endMatch = getInchSymbol(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.INCHSYMBOL, input.substring(j, i)));
            } else if (isPointSymbol(input.substring(i, N))) {
                int endMatch = getPointSymbol(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.POINTSYMBOL, input.substring(j, i)));
            } else {
                i++;
                return (new Token(Type.DIVIDE));
            }
        } else return null;
    }
}
