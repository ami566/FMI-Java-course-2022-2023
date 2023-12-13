package bg.sofia.uni.fmi.mjt.sentiment;

public class Word {
    private String word;
    private int count = 0;
    private int actualCount = 0;
    private double score = 0;
    private double sentiment = 0;

    public Word(String word) {
        this.word = word;
    }

    public void addScore(double sc) {
        score += sc;
        count++;
        increaseCount();
        calculateSentiment();
    }

    public void increaseCount() {
        actualCount++;
    }

    public int getActualCount() {
        return actualCount;
    }

    public String getText() {
        return word;
    }

    public double getSentiment() {
        return sentiment;
    }

    private void calculateSentiment() {
        sentiment = score / count;
    }
}
