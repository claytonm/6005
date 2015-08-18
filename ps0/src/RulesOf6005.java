import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * RulesOf6005 represents some of the rules of 6.005 as described by the 
 * general information on Stellar. 
 *
 * The course elements are described by the hasFeature function. 
 *
 * The grade details are described by the computeGrade function.
 *
 * The extension policy (slack days) are described by the extendDeadline function.
 */
public class RulesOf6005 {


	/**
	 * Tests if the string is one of the items in the Course Elements section. 
	 *
	 * @param name - the element to be tested
	 * @return true if <name> appears in bold in Course Elements section. Ignores case (capitalization). 
	 * Example: "Lectures" and "lectures" will both return true.
	 */
	public static boolean hasFeature(String name){

		// TODO: Fill in this method, then remove the RuntimeException
		// throw new RuntimeException("hasFeature not implemented");
		// The name of the file to open.
		String urlName = "http://ocw.mit.edu/courses/electrical-engineering-"
				+ "and-computer-science/6-005-elements-of-software-construction-fall-2011/Syllabus/";
		try {
			Document doc = Jsoup.connect(urlName).get();
			Elements links = doc.select("h2:contains(Course Elements) ~ h3");
			boolean containsName = links.text().toString().toLowerCase().contains(name.toLowerCase());
			return containsName;
		}
		catch(IOException ex) {
			return false;
			// ex.printStackTrace();
		}
	}


	/**
	 * Takes in the quiz, pset, project, and participation grades as values out of a 
	 * hundred and returns the grade based on the course information also as a value out 
	 * of a hundred, rounded to the nearest integer. 
	 *
	 * Behavior is unspecified if the values are out of range.
	 *
	 * @param quiz
	 * @param pset
	 * @param project
	 * @param participation
	 * @return the resulting grade out of a hundred
	 */
	public static int computeGrade(int quiz, int pset, int project, int participation){
		// TODO: Fill in this method, then remove the RuntimeException
		// throw new RuntimeException("computeGrade not implemented");
		return (int) Math.round(0.2 * quiz + 0.4 * pset + 0.3 * project + 0.1 * participation);
	}


	/**
	 * Based on the slack day policy, returns a date of when the assignment would be due, making sure not to
	 * exceed the budget. In the case of the request being more than what's allowed, the latest possible
	 * due date is returned. 
	 *
	 * Hint: Take a look at http://download.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html
	 *
	 * Behavior is unspecified if request is negative or duedate is null. 
	 *
	 * @param request - the requested number of slack days to use
	 * @param budget - the total number of available slack days
	 * @param duedate - the original due date of the assignment
	 * @return a new instance of a Calendar with the date and time set to when the assignment will be due
	 */
	public static Calendar extendDeadline(int request, int budget, Calendar duedate) {
		// TODO: Fill in this method, then remove the RuntimeException
		// throw new RuntimeException("extendDeadline not implemented");
		// Calendar newdate = new GregorianCalendar();
		// newdate.setTime(duedate);
		if (request <= budget) {
			duedate.add(Calendar.DATE, request);
		}
		else {
			duedate.add(Calendar.DATE, budget);
		}
		return duedate;
	}


	/**
	 * Main method of the class. Runs the functions hasFeature, computeGrade, and 
	 * extendDeadline. 
	 *
	 * @param args
	 */
	public static void main(String[] args){
		// hasFeature("QUIZZES");
		System.out.println("Has feature QUIZZES: " + hasFeature("QUIZZES"));
		System.out.println("My grade is: " + computeGrade(60, 40, 50, 37));
		Calendar duedate = new GregorianCalendar();
		duedate.set(2011, 8, 9, 23, 59, 59);
		System.out.println("Original due date: " + duedate.getTime());
		System.out.println("New due date:" + extendDeadline(4, 2, duedate).getTime());
	}
}
