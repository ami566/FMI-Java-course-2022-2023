package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

import java.util.Random;

public class Student implements Runnable {

    private final int fn;
    private final String name;
    private StudentGradingAPI studentGradingAPI;
    private Assignment assignment;
    private Random rand = new Random();
    private final int maxNum = 4;
    private final int maxTime = 1001;
    private int timeForAssignment;

    public Student(int fn, String name, StudentGradingAPI studentGradingAPI) {
        this.fn = fn;
        this.name = name;
        this.studentGradingAPI = studentGradingAPI;

        timeForAssignment = rand.nextInt(maxTime);
        int num = rand.nextInt(0, maxNum);
        AssignmentType type = switch (num) {
            case 0 -> AssignmentType.LAB;
            case 1 -> AssignmentType.HOMEWORK;
            case 2 -> AssignmentType.PLAYGROUND;
            default -> AssignmentType.PROJECT;
        };
        assignment = new Assignment(fn, name, type);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeForAssignment);
        } catch (InterruptedException e) {
            System.err.print("Unexpected exception was thrown: " + e.getMessage());
            e.printStackTrace();
        }
        studentGradingAPI.submitAssignment(assignment);
    }

    public int getFn() {
        return fn;
    }

    public String getName() {
        return name;
    }

    public StudentGradingAPI getGrader() {
        return studentGradingAPI;
    }

}