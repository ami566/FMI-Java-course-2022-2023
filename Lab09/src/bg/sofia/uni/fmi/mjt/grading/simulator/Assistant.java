package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;

import java.util.concurrent.atomic.AtomicInteger;

public class Assistant extends Thread {
    private final String name;
    private AdminGradingAPI grader;
    private AtomicInteger gradedAssignments;

    public Assistant(String name, AdminGradingAPI grader) {
        this.name = name;
        this.grader = grader;
        gradedAssignments = new AtomicInteger(0);
    }

    @Override
    public void run() {
        Assignment assignment = null;

        while ((assignment = grader.getAssignment()) != null) {
            try {

                Thread.sleep(assignment.type().getGradingTime());

            } catch (InterruptedException e) {
                System.err.print("Unexpected exception was thrown: " + e.getMessage());
                e.printStackTrace();
            }
            gradedAssignments.incrementAndGet();
        }
    }

    public int getNumberOfGradedAssignments() {
        return gradedAssignments.get();
    }
}