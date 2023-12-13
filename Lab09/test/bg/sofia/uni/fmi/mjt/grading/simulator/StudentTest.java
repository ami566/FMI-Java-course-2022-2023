package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTest {
    private static CodePostGrader grader;
    private static Student student;
    private static final String name = "Amelia";
    private static final int fn = 66666;

    @BeforeAll
    public static void setUp() {
        grader = new CodePostGrader(3);
        student = new Student(fn, name, grader);
    }

    @Test
    void testGetFn() {
        assertEquals(fn, student.getFn(), "Incorrect fn");
        student.run();
        grader.finalizeGrading();
    }

    @Test
    void testGetName() {
        assertEquals(name, student.getName(), "Incorrect name");
        student.run();
        grader.finalizeGrading();
    }

    @Test
    void testGetGrader() {
        assertEquals(grader, student.getGrader(), "Incorrect grader");
        student.run();
        grader.finalizeGrading();
    }
}
