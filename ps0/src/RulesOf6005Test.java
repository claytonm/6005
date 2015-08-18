import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

/**
 * JUnit tests for RulesOf6005. Goes through the methods
 * hasFeature, testComputeGrade, and testExtendedDueDate
 *
 */
public class RulesOf6005Test {

    /**
     * Tests the hasFeature method of RulesOf6005. 
     */
	@Test
	public void testHasFeature() {
		assertTrue(RulesOf6005.hasFeature("QUIZZES"));
		assertFalse(RulesOf6005.hasFeature("quuuuuiiizes"));
		assertFalse(RulesOf6005.hasFeature("Laptops not required"));
		assertTrue(RulesOf6005.hasFeature("code review"));
	}
	
	
	/**
	 * Tests the computeGrade method of RulesOf6005.
	 */
	@Test
	public void testComputeGrade(){
		assertEquals(100, RulesOf6005.computeGrade(100, 100, 100, 100));
		assertEquals(0, RulesOf6005.computeGrade(0, 0, 0, 0));
		assertEquals(47, RulesOf6005.computeGrade(60, 40, 50, 37));
	}


	/**
	 * Tests the extendDeadline method of Rules of6005.
	 */
	@Test
	public void testExtendDeadline(){
        Calendar duedate = new GregorianCalendar();
        duedate.clear();
        duedate.set(2011, 8, 9, 23, 59, 59);
        Calendar twoDaysAfter = new GregorianCalendar();
        twoDaysAfter.clear();
        twoDaysAfter.set(2011, 8, 11, 23, 59, 59);
        Calendar extended = RulesOf6005.extendDeadline(4, 2, duedate);
        assertEquals(extended.get(Calendar.YEAR), twoDaysAfter.get((Calendar.YEAR)));
        assertEquals(extended.get(Calendar.MONTH), twoDaysAfter.get((Calendar.MONTH)));
        assertEquals(extended.get(Calendar.DATE), twoDaysAfter.get((Calendar.DATE)));
        assertEquals(extended.get(Calendar.HOUR), twoDaysAfter.get((Calendar.HOUR)));
        assertEquals(extended.get(Calendar.MINUTE), twoDaysAfter.get((Calendar.MINUTE)));
        assertEquals(extended.get(Calendar.SECOND), twoDaysAfter.get((Calendar.SECOND)));

        duedate = new GregorianCalendar();
        duedate.clear();
        duedate.set(2011, 8, 9, 23, 59, 59);
        Calendar threeDaysAfter = new GregorianCalendar();
        threeDaysAfter.clear();
        threeDaysAfter.set(2011, 8, 12, 23, 59, 59);
        extended = RulesOf6005.extendDeadline(3, 5, duedate);
        assertEquals(extended.get(Calendar.YEAR), threeDaysAfter.get((Calendar.YEAR)));
        assertEquals(extended.get(Calendar.MONTH), threeDaysAfter.get((Calendar.MONTH)));
        assertEquals(extended.get(Calendar.DATE), threeDaysAfter.get((Calendar.DATE)));
        assertEquals(extended.get(Calendar.HOUR), threeDaysAfter.get((Calendar.HOUR)));
        assertEquals(extended.get(Calendar.MINUTE), threeDaysAfter.get((Calendar.MINUTE)));
        assertEquals(extended.get(Calendar.SECOND), threeDaysAfter.get((Calendar.SECOND)));
	}

}
