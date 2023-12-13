package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodePostGraderTest {
    private static final int countOfAssistants = 5;
    private static final int countOfStudents = 10;
    private static CodePostGrader grader = new CodePostGrader(countOfAssistants);;
    @BeforeEach
    public static void setUp(){
        grader = new CodePostGrader(countOfAssistants);
        ArrayList<Student> students = new ArrayList<Student>();

        for(int i = 0; i < countOfStudents; i++) {
            Student stud = new Student(i + 1, "Amelia" + i, grader);
            students.add(stud);
        }
        for(int i = 0; i < countOfStudents; i++) {
            students.get(i).run();
        }
    }
    @Test
    void testGetAssistants(){
        assertEquals(countOfAssistants, grader.getAssistants().size(),
                "Count of assistants does not match");
        grader.finalizeGrading();
    }

    @Test
    void testGetSubmittedAssignments(){
        assertEquals(countOfStudents, grader.getSubmittedAssignmentsCount(),
                "Error with the submitted assignments");
        grader.finalizeGrading();
    }

    @Test
    void testGetAssignmentEmpty(){
        assertEquals(null, grader.getAssignment(),
                "When finalized, should return null");
        grader.finalizeGrading();
    }
}
