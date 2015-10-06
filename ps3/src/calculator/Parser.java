package calculator;


import calculator.Lexer.Token;
import calculator.Lexer.Token.*;


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

    Lexer lexer;
    // String expression;
    private static final double PT_PER_IN = 72;

	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {
	}

	/**
	 * Type of values.
	 */
	public enum ValueType {
		POINTS, INCHES, SCALAR
	};

    public class Expression {

        Token operator;
        Token leaf = null;
        Expression leftExpression;
        Expression rightExpression;

        public Expression(Token leaf) {
            this.leaf = leaf;
        }

        public Expression(Token operator, Expression leftExpression, Expression rightExpression) {
            this.operator = operator;
            this.leftExpression = leftExpression;
            this.rightExpression = rightExpression;
        }

        public Boolean isLeaf() {
            return this.leaf != null;
        }
    }

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

    Parser (Lexer lexer) {
        this.lexer = lexer;
    }

	// TODO write method spec

    public Expression Parser(Lexer lexer) {
        Expression expression;
        Token token = lexer.next();
        if (token != null) {
            if (token.type == Type.SCALAR | token.type == Type.POINTS | token.type == Type.INCHES) {
                expression = new Expression(token);
                return expression;
            } else {
                Token operator;
                Expression leftExpression;
                Expression rightExpression;
                leftExpression = Parser(lexer);
                operator = lexer.next();
                if (operator != null) {
                    if (operator.type == Type.CLOSEPAREN) {
                        return leftExpression;
                    } else if (operator.type == Type.INCHSYMBOL | operator.type == Type.POINTSYMBOL) {
                        Expression rightLeftExpression;
                        if (operator.type == Type.INCHSYMBOL) {
                            rightLeftExpression = new Expression(Lexer.makeInch());
                        } else {
                            rightLeftExpression = new Expression(Lexer.makePoint());
                        }
                        Expression UnitConversion = new Expression(Lexer.makeMultiply(), rightLeftExpression, leftExpression);
                        leftExpression = UnitConversion;
                        operator = lexer.next();
                    }
                    if (operator != null) {
                        rightExpression = Parser(lexer);
                        Token rightOperator = lexer.next();
                        if (rightOperator != null) {
                            if (rightOperator.type == Type.CLOSEPAREN) {
                                return new Expression(operator, leftExpression, rightExpression);
                                // return new Expression(operator, leftExpression, rightExpression);
                            } else if (rightOperator.type == Type.INCHSYMBOL | operator.type == Type.POINTSYMBOL) {
                                Expression rightLeftExpression;
                                if (rightOperator.type == Type.INCHSYMBOL) {
                                    rightLeftExpression = new Expression(Lexer.makeInch());
                                } else {
                                    rightLeftExpression = new Expression(Lexer.makePoint());
                                }
                                Expression UnitConversion = new Expression(Lexer.makeMultiply(), rightLeftExpression, rightExpression);
                                rightExpression = UnitConversion;
                            }
                        }
                    } else {
                        return leftExpression;
                    }
                } else {
                    return leftExpression;
                }
            }
        } else {
            return null;
        }
        return null;
    }



    public Value apply(Token operator, Value leftOperand, Value rightOperand) {
        Type operatorType = operator.getOperatorType();
        ValueType leftType = leftOperand.type;
        ValueType rightType = rightOperand.type;
        Double leftValue = leftOperand.value;
        Double rightValue = rightOperand.value;

        switch (operatorType) {
            case PLUS:
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue + rightValue, rightType));
                }
                else {
                    return(new Value(leftValue + rightValue, leftType));
                }
            case MINUS:
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue - rightValue, rightType));
                }
                else {
                    return(new Value(leftValue - rightValue, leftType));
                }
            case MULTIPLY:
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue*rightValue, rightType));
                }
                else if (rightType == ValueType.SCALAR){
                    return(new Value(leftValue*rightValue, leftType));
                }
                else if (leftType == rightType) {
                    return(new Value(leftValue*rightValue, leftType));
                }
                else if (leftType == ValueType.INCHES & rightType == ValueType.POINTS) {
                    return(new Value(leftValue*rightValue, ValueType.SCALAR));
                }
                else {
                    return(new Value(leftValue*rightValue, ValueType.SCALAR));
                }
            case DIVIDE:
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue/rightValue, rightType));
                }
                else if (rightType == ValueType.SCALAR){
                    return(new Value(leftValue/rightValue, leftType));
                }
                else {
                    return(new Value(leftValue/rightValue, ValueType.SCALAR));
                }
        }
        return null;
    }

	// TODO write method spec
	public Value evaluate(String expression) {
		// TODO implement for Problem 4
        Lexer lexer = new Lexer(expression);
        Expression parsed = Parser(lexer);
        return evaluateIter(parsed);
	}

    public Value evaluateIter(Expression parsed) {
        if (parsed.isLeaf()) {
            Token token = parsed.leaf;
            return(new Value(token.getTokenValue(), token.getTokenType()));
        }
        else {
            Token operator = parsed.operator;
            Expression leftExpression = parsed.leftExpression;
            Expression rightExpression = parsed.rightExpression;
            return apply(operator, evaluateIter(leftExpression), evaluateIter(rightExpression));
            }
        }
    }
