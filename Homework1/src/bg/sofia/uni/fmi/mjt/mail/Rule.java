package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rule {
    private String from;
    private List<String> recipients;
    private List<String> subjectIncludes;
    private List<String> subjectOrBodyIncludes;
    private Folder folder;
    private int priority;
    private String ruleDefinition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return priority == rule.priority && Objects.equals(folder, rule.folder) && Objects.equals(ruleDefinition, rule.ruleDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folder, priority, ruleDefinition);
    }

    public Rule() {
        subjectIncludes = new ArrayList<>();
        recipients = new ArrayList<>();
        subjectOrBodyIncludes = new ArrayList<>();
    }

    public Rule(String ruleDefinition, Folder folder, int priority) {
        this.ruleDefinition = ruleDefinition;
        this.folder = folder;
        this.priority = priority;
        String[] conditions = ruleDefinition.split(System.lineSeparator());
        for (String c : conditions) {
            String[] elements = c.split(":");
            String[] keywords = elements[1].split(",");
            if (elements[0].equals("subject-includes")) {
                if (subjectIncludes != null) {
                    throw new RuleAlreadyDefinedException("subject-includes is already defined");
                }
                subjectIncludes = new ArrayList<>();
                for (String word : keywords) {
                    subjectIncludes.add(word.strip());
                }
            }
            if (elements[0].equals("subject-or-body-includes")) {
                if (subjectOrBodyIncludes != null) {
                    throw new RuleAlreadyDefinedException("subject-or-body-includes is already defined");
                }
                subjectOrBodyIncludes = new ArrayList<>();
                for (String word : keywords) {
                    subjectOrBodyIncludes.add(word.strip());
                }
            }
            if (elements[0].equals("recipients-includes")) {
                if (recipients != null) {
                    throw new RuleAlreadyDefinedException("recipients-includes is already defined");
                }
                recipients = new ArrayList<>();
                for (String word : keywords) {
                    recipients.add(word.strip());
                }
            }
            if (elements[0].equals("from")) {
                if (from != null) {
                    throw new RuleAlreadyDefinedException("from is already defined");
                }
                from = elements[1].strip();
            }
        }
    }
}
