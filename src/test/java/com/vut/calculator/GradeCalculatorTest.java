package com.vut.calculator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the GradeCalculator class.
 * 
 * INSTRUCTIONS FOR STUDENTS:
 * --------------------------
 * Some tests have been written for you. Run them — they will FAIL because
 * the GradeCalculator code contains logic errors.
 * 
 * Your tasks:
 *   1. Run these tests and observe which ones fail.
 *   2. Complete the TODO tests (marked below).
 *   3. Identify and fix the bugs in GradeCalculator.java so all tests pass.
 *   4. Integrate these tests into your Jenkins CI/CD pipeline.
 *
 * DO NOT modify the expected values in the tests — the tests are CORRECT.
 * The bugs are in GradeCalculator.java.
 */
public class GradeCalculatorTest {

    private GradeCalculator calculator;

    @Before
    public void setUp() {
        calculator = new GradeCalculator();
    }

    // =====================================================================
    // TEST GROUP 1: Final Mark Calculation
    // Rule: Final Mark = (Semester Mark × 0.4) + (Exam Mark × 0.6)
    // =====================================================================

    @Test
    public void testFinalMarkCalculation_StandardCase() {
        // Semester=60, Exam=70 → (60*0.4)+(70*0.6) = 24+42 = 66.0
        assertEquals(66.0, calculator.calculateFinalMark(60, 70), 0.01);
    }

    @Test
    public void testFinalMarkCalculation_HighMarks() {
        // Semester=90, Exam=85 → (90*0.4)+(85*0.6) = 36+51 = 87.0
        assertEquals(87.0, calculator.calculateFinalMark(90, 85), 0.01);
    }

    @Test
    public void testFinalMarkCalculation_LowMarks() {
        // Semester=30, Exam=40 → (30*0.4)+(40*0.6) = 12+24 = 36.0
        assertEquals(36.0, calculator.calculateFinalMark(30, 40), 0.01);
    }

    // =====================================================================
    // TEST GROUP 2: Grade Determination
    // 80-100=Distinction, 70-79=Merit, 60-69=Credit, 50-59=Pass, 0-49=Fail
    // =====================================================================

    @Test
    public void testGrade_Distinction() {
        assertEquals("Distinction", calculator.determineGrade(85));
    }

    @Test
    public void testGrade_DistinctionBoundary() {
        // Exactly 80 should be Distinction
        assertEquals("Distinction", calculator.determineGrade(80));
    }

    @Test
    public void testGrade_Merit() {
        assertEquals("Merit", calculator.determineGrade(75));
    }

    @Test
    public void testGrade_MeritLowerBoundary() {
        assertEquals("Merit", calculator.determineGrade(70));
    }

    @Test
    public void testGrade_Credit() {
        assertEquals("Credit", calculator.determineGrade(65));
    }

    @Test
    public void testGrade_CreditLowerBoundary() {
        assertEquals("Credit", calculator.determineGrade(60));
    }

    @Test
    public void testGrade_Pass() {
        assertEquals("Pass", calculator.determineGrade(55));
    }

    @Test
    public void testGrade_PassLowerBoundary() {
        // Exactly 50 should be Pass
        assertEquals("Pass", calculator.determineGrade(50));
    }

    @Test
    public void testGrade_Fail() {
        assertEquals("Fail", calculator.determineGrade(49));
    }

    @Test
    public void testGrade_FailZero() {
        assertEquals("Fail", calculator.determineGrade(0));
    }

    // =====================================================================
    // TEST GROUP 3: Exam Admission
    // Minimum semester mark of 40 required
    // =====================================================================

    @Test
    public void testExamAdmission_Admitted() {
        assertTrue(calculator.hasExamAdmission(50));
    }

    @Test
    public void testExamAdmission_BoundaryAdmitted() {
        // Exactly 40 should be admitted
        assertTrue(calculator.hasExamAdmission(40));
    }

    @Test
    public void testExamAdmission_Denied() {
        assertFalse(calculator.hasExamAdmission(39));
    }

    // TODO: Write a test for a student with semester mark of 42
    // (between the buggy threshold of 45 and correct threshold of 40)
    @Test
    public void testExamAdmission_Between40And45() {
        // A semester mark of 42 is >= 40, so the student SHOULD be admitted.
        // BUG #3 uses >= 45, so this returns false incorrectly.
        assertTrue("Semester mark of 42 should qualify for exam admission", calculator.hasExamAdmission(42));
    }

    // =====================================================================
    // TEST GROUP 4: Class Average Calculation
    // =====================================================================

    @Test
    public void testClassAverage_Normal() {
        double[] marks = {60, 70, 80};
        // Average = (60+70+80)/3 = 70.0
        assertEquals(70.0, calculator.calculateClassAverage(marks), 0.01);
    }

    @Test
    public void testClassAverage_SingleStudent() {
        double[] marks = {55};
        assertEquals(55.0, calculator.calculateClassAverage(marks), 0.01);
    }

    @Test
    public void testClassAverage_EmptyArray() {
        double[] marks = {};
        assertEquals(0.0, calculator.calculateClassAverage(marks), 0.01);
    }

    // TODO: Write a test for class average with 5 students
    @Test
    public void testClassAverage_FiveStudents() {
        // Marks: 45, 55, 65, 75, 85
        // Expected average: (45+55+65+75+85)/5 = 325/5 = 65.0
        // BUG #4 divides by (length+1)=6, producing 54.17 instead.
        double[] marks = {45, 55, 65, 75, 85};
        assertEquals(65.0, calculator.calculateClassAverage(marks), 0.01);
    }

    // =====================================================================
    // TEST GROUP 5: Pass Rate Calculation
    // Pass = final mark >= 50
    // =====================================================================

    @Test
    public void testPassRate_AllPass() {
        double[] marks = {60, 70, 80, 90};
        // 4 out of 4 pass = 100% → 1.0 as a ratio, but we want percentage
        // Pass rate should be 100.0 (as percentage)
        assertEquals(100.0, calculator.calculatePassRate(marks) * 100, 0.01);
    }

    @Test
    public void testPassRate_MixedResults() {
        double[] marks = {30, 45, 50, 65, 80};
        // Students with >= 50: 50, 65, 80 = 3 out of 5 = 0.6
        assertEquals(0.6, calculator.calculatePassRate(marks), 0.01);
    }

    // TODO: Write a test for pass rate where no students pass
    @Test
    public void testPassRate_NonePass() {
        // All marks below 50 — pass rate should be 0.0
        // BUG #5 uses >= 55 as threshold, so marks of 50-54 are wrongly excluded too.
        double[] marks = {10, 20, 30, 40, 49};
        assertEquals(0.0, calculator.calculatePassRate(marks), 0.01);
    }

    // =====================================================================
    // TEST GROUP 6: Highest Mark Finder
    // =====================================================================

    @Test
    public void testHighestMark_Normal() {
        double[] marks = {55, 78, 62, 91, 45};
        assertEquals(91.0, calculator.findHighestMark(marks), 0.01);
    }

    @Test
    public void testHighestMark_AllSame() {
        double[] marks = {70, 70, 70};
        assertEquals(70.0, calculator.findHighestMark(marks), 0.01);
    }

    // TODO: Write a test where highest mark is at the beginning of the array
    @Test
    public void testHighestMark_FirstElement() {
        // Highest mark is the first element — BUG #6 uses < instead of >,
        // so it finds the LOWEST mark (45) instead of the highest (95).
        double[] marks = {95, 60, 72, 45, 83};
        assertEquals(95.0, calculator.findHighestMark(marks), 0.01);
    }

    // =====================================================================
    // TEST GROUP 7: Supplementary Exam Eligibility
    // Eligible if final mark is between 45 and 49 (inclusive)
    // =====================================================================

    @Test
    public void testSupplementary_Eligible() {
        assertTrue(calculator.qualifiesForSupplementary(47));
    }

    @Test
    public void testSupplementary_LowerBoundary() {
        assertTrue(calculator.qualifiesForSupplementary(45));
    }

    @Test
    public void testSupplementary_UpperBoundary() {
        assertTrue(calculator.qualifiesForSupplementary(49));
    }

    @Test
    public void testSupplementary_TooLow() {
        assertFalse(calculator.qualifiesForSupplementary(44));
    }

    @Test
    public void testSupplementary_TooHigh() {
        assertFalse(calculator.qualifiesForSupplementary(50));
    }

    // =====================================================================
    // TEST GROUP 8: Mark Validation
    // Valid marks are between 0 and 100 inclusive
    // =====================================================================

    @Test
    public void testValidMark_InRange() {
        assertTrue(calculator.isValidMark(50));
    }

    @Test
    public void testValidMark_Zero() {
        assertTrue(calculator.isValidMark(0));
    }

    @Test
    public void testValidMark_Hundred() {
        assertTrue(calculator.isValidMark(100));
    }

    @Test
    public void testValidMark_Negative() {
        assertFalse(calculator.isValidMark(-1));
    }

    @Test
    public void testValidMark_Over100() {
        assertFalse(calculator.isValidMark(101));
    }

    // TODO: Write a test for mark value of 105 (should be invalid)
    @Test
    public void testValidMark_WayOver100() {
        // 105 is outside the valid range of 0-100.
        // BUG #8 uses -10 to 110 as the valid range, so 105 wrongly returns true.
        assertFalse("105 is not a valid mark", calculator.isValidMark(105));
    }
}
