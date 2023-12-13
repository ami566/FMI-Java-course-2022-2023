import bg.sofia.uni.fmi.mjt.grading.simulator.Student;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        CodePostGrader grader = new CodePostGrader(5);

        ArrayList<Student> students = new ArrayList<Student>();

        for(int i = 0; i < 30; i++) {
            Student stud = new Student(i + 1, "Amelia" + i, grader);
            students.add(stud);
        }


        for(int i = 0; i < 30; i++) {
            students.get(i).run();
        }

        grader.finalizeGrading();
    }
}