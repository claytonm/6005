package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import calculator.Lexer;


/**
 * Multi-unit calculator.
 */
public class MultiUnitCalculator {

//	/**
//	 * @param expression
//	 *            a String representing a multi-unit expression, as defined in
//	 *            the problem set
//	 * @return the value of the expression, as a number possibly followed by
//	 *         units, e.g. "72pt", "3", or "4.882in"
//	 */
//	public String evaluate(String expression) {
//        // TODO implement for Problem 4
//        return expression;
//    }
//	/**
//	 * Repeatedly reads expressions from the console, and outputs the results of
//	 * evaluating them. Inputting an empty line will terminate the program.
//	 *
//	 * @param args
//	 *            unused
//	 */
	public static void main(String[] args) throws IOException {
		MultiUnitCalculator calculator;
		 String result;

		 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		 String expression;
		while (true) {
			// display prompt
			System.out.print("> ");
			// read input
			expression = in.readLine();
            System.out.println(expression);
//			// terminate if input empty
			Lexer lexer = new Lexer(expression);
////			while (lexer.hasNext()) {
////				System.out.println(lexer.next().type);
////			}
			Parser parser = new Parser(lexer);
            System.out.println(parser.Parser(lexer));
			// evaluate
			// calculator = new MultiUnitCalculator();
			// result = calculator.evaluate(expression);
			// display result
			// System.out.println(result);
		}
	}
 }
