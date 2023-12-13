package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class CodePostGrader implements AdminGradingAPI {
    private AtomicInteger submittedAssignments;
    Queue<Assignment> notGradedAssignments;
    List<Assistant> assistants;
    private boolean isFinalized = false;

    public CodePostGrader(int numberOfAssistants) {
        notGradedAssignments = new LinkedList<>();
        assistants = new ArrayList<>();
        submittedAssignments = new AtomicInteger(0);

        for (int i = 0; i < numberOfAssistants; i++) {
            assistants.add(new Assistant("assistant" + i, this));
            assistants.get(i).start();
        }
    }

    @Override
    public synchronized Assignment getAssignment() {
        while (!isFinalized && notGradedAssignments.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return notGradedAssignments.isEmpty() ? null : notGradedAssignments.remove();
    }

    @Override
    public int getSubmittedAssignmentsCount() {
        return submittedAssignments.get();
    }

    @Override
    public synchronized void finalizeGrading() {
        isFinalized = true;
        this.notifyAll();
    }

    @Override
    public List<Assistant> getAssistants() {
        return assistants;
    }

    @Override
    public void submitAssignment(Assignment assignment) {
        synchronized (this) {
            if (!isFinalized) {
                notGradedAssignments.add(assignment);
            }
            this.notifyAll();
        }
        submittedAssignments.incrementAndGet();
    }
}
