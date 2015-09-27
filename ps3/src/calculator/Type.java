package calculator;

/*
 * Tokens will include numbers, units, operators, and parenthesis.
 * Numbers can be either integer or float-point.
 * Units enums are POINT and INCH
 * Operators are grouped into four sub-groups: PLUS {+}, MINUS {-}, MULTIPLY {*}, and DIVIDE {/}
 * OPENPAREN: {(}
 * CLOSEPAREN: {)}
 */

/**
 * Token type.
 */
enum Type {
	SCALAR,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    INCHES,
    POINTS,
    OPENPAREN,
    CLOSEPAREN,
    POINTSYMBOL,
    INCHSYMBOL
}