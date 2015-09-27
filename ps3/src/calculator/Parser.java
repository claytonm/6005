package calculator;

import calculator.Lexer;
import calculator.Lexer.Token;
import java.util.ArrayList;

/*
 * Expression ::= number | number unit | expression operator expression
 * operator ::= {+, -, *, /}
 * unit ::= {pt, in}
 * number ::= [0-9]+[\\.[0-9]+]?
 *
 */

/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {

    // String expression;
    private static final double PT_PER_IN = 72;
    private final Lexer lexer;

	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {
	}

	/**
	 * Type of values.
	 */
	private enum ValueType {
		POINTS, INCHES, SCALAR
	};

	/**
	 * Internal value is always in points.
	 */
	public class Value {
		final double value;
		final ValueType type;

		Value(double value, ValueType type) {
			this.value = value;
			this.type = type;
		}

		@Override
		public String toString() {
			switch (type) {
			case INCHES:
				return value / PT_PER_IN + " in";
			case POINTS:
				return value + " pt";
			default:
				return "" + value;
			}
		}
	}


    Parser(Lexer lexer) {
        // this.expression = expression;
        this.lexer = lexer;
    }

	// TODO write method spec

	public ArrayList<String> Parser(Lexer lexer) {
        ArrayList returnArray = new ArrayList<String>();
		// TODO implement for Problem 3
        if (lexer.hasNext()) {
            Token token = lexer.next();
            if (token.type == Type.SCALAR | token.type == Type.POINTS | token.type == Type.INCHES) {
                returnArray.add(token.text);
                return (returnArray);
            } else {
                Token operator;
                ArrayList leftValue;
                ArrayList rightValue;
                leftValue = Parser(lexer);
                if (lexer.hasNext()) {
                    operator = lexer.next();
                    if (operator.type == Type.INCHSYMBOL | operator.type == Type.POINTSYMBOL) {
                        ArrayList UnitConversion = new ArrayList<>();
                        UnitConversion.add(Lexer.makeMultiply().text);
                        UnitConversion.add(leftValue);
                        if (operator.type == Type.INCHSYMBOL) {
                            UnitConversion.add(Lexer.makeInch().text);
                        } else {
                            UnitConversion.add(Lexer.makePoint().text);
                        }
                        leftValue = UnitConversion;
                        if (lexer.hasNext()) {
                            operator = lexer.next();
                            if (operator.type == Type.CLOSEPAREN) {
                                return leftValue;
                            }
                            else {
                                returnArray.add(operator.text);
                            }
                        }
                    }
                    else if (operator.type == Type.CLOSEPAREN) {
                        return leftValue;
                    }
                    else {
                        returnArray.add(operator.text);
                    }
                }
                returnArray.add(leftValue);
                if (lexer.hasNext()) {
                    rightValue = Parser(lexer);
                    returnArray.add(rightValue);
                    if (lexer.hasNext()) {
                        Token nextToken = lexer.next();
                        if (nextToken.type == Type.INCHSYMBOL | nextToken.type == Type.POINTSYMBOL) {
                            ArrayList UnitConversion = new ArrayList<>();
                            UnitConversion.add(Lexer.makeMultiply().text);
                            UnitConversion.add(returnArray);
                            if (nextToken.type == Type.INCHSYMBOL) {
                                UnitConversion.add(Lexer.makeInch().text);
                            } else {
                                UnitConversion.add(Lexer.makePoint().text);
                            }
                            return (UnitConversion);
                        }
                    }
                    return (returnArray);
                }
            }
        }
        else {
            return(returnArray);
        }
        return(returnArray);
	}





	// TODO write method spec
	public Value evaluate(String expression) {
		// TODO implement for Problem 4
        Lexer lexer = new Lexer(expression);
        ArrayList<String> parsed = Parser(lexer);

        String operator = parsed.get(0);
        return null;


	}
}
