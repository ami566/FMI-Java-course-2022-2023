package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {
    private Map<String, Word> words;
    private Set<String> stopWords;
    private Writer reviewsOut;
    private Reader reviewsIn;
    private Reader stopWordsIn;
    private final int negative = 0;
    private final int somewhatNegative = 1;
    private final int neutral = 2;
    private final int somewhatPositive = 3;
    private final int positive = 4;
    private final String negText = "negative";
    private final String somewhatNegText = "somewhat negative";
    private final String neutralText = "neutral";
    private final String somewhatPosText = "somewhat positive";
    private final String posText = "positive";
    private final String unknown = "unknown";
    private final char zero = '0';


    // private static final DecimalFormat df = new DecimalFormat("0.000");

    private void initStopWordsList() {
        stopWords = new HashSet<>();
        try (var in = new BufferedReader(stopWordsIn)) {
            String line;
            while ((line = in.readLine()) != null) {
                stopWords.add(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    private void addWordToDictionary(String word, int sentiment) {
        if (!words.containsKey(word)) {
            words.put(word, new Word(word));
        }
        words.get(word).addScore(sentiment);
    }

    private void getWords(String line, int sentiment) {
        Set<String> s = new HashSet<>();
        String[] wordsArray = line.split("[^a-zA-Z']+");
        for (String w : wordsArray) {
            w = w.toLowerCase();
            if (!isStopWord(w) && w.length() >= neutral) {
                if (!s.contains(w)) {
                    s.add(w);
                    addWordToDictionary(w, sentiment);
                } else {
                    words.get(w).increaseCount();
                }
            }
        }
    }

    private void initWordsList() {
        words = new HashMap<>();
        try (var in = new BufferedReader(reviewsIn)) {
            String line;
            while ((line = in.readLine()) != null) {
                int sentiment = line.charAt(0) - zero;
                getWords(line.trim(), sentiment);
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    public MovieReviewSentimentAnalyzer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {
        this.reviewsOut = reviewsOut;
        this.reviewsIn = reviewsIn;
        this.stopWordsIn = stopwordsIn;
        initStopWordsList();
        initWordsList();
    }

    @Override
    public double getReviewSentiment(String review) {
        double score = 0;
        int knownWords = 0;
        String[] wordsArray = review.split("[^a-zA-Z']+");
        for (String w : wordsArray) {
            w = w.toLowerCase();
            if (!isStopWord(w) && w.length() >= neutral) {
                if (words.containsKey(w)) {
                    score += words.get(w).getSentiment();
                    knownWords++;
                }
            }
        }
        return knownWords == 0 ? -1.0 : score / knownWords;
    }

    @Override
    public String getReviewSentimentAsName(String review) {
        int score = (int) Math.round(getReviewSentiment(review));
        String sentiment = switch (score) {
            case negative -> negText;
            case somewhatNegative -> somewhatNegText;
            case neutral -> neutralText;
            case somewhatPositive -> somewhatPosText;
            case positive -> posText;
            default -> unknown;
        };
        return sentiment;
    }

    @Override
    public double getWordSentiment(String word) {
        word = word.toLowerCase();
        return words.containsKey(word) ? words.get(word).getSentiment() : -1;
    }

    @Override
    public int getWordFrequency(String word) {
        word = word.toLowerCase();
        if (stopWords.contains(word)) {
            return -1;
        }
        return words.containsKey(word) ? words.get(word).getActualCount() : 0;
    }

    @Override
    public List<String> getMostFrequentWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number cannot be negative");
        }

        if (n > words.size()) {
            n = words.size();
        }

        List<Word> w = words.values().stream().toList();

        return w.stream().sorted(Comparator.comparingInt(Word::getActualCount).reversed()).
                map(Word::getText).limit(n).toList();
    }

    @Override
    public List<String> getMostPositiveWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number cannot be negative");
        }
        if (n > words.size()) {
            n = words.size();
        }

        List<Word> w = words.values().stream().toList();
        return w.stream().sorted(Comparator.comparing(Word::getSentiment).reversed()).
                map(Word::getText).limit(n).toList();
    }

    @Override
    public List<String> getMostNegativeWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number cannot be negative");
        }
        if (n > words.size()) {
            n = words.size();
        }

        List<Word> w = words.values().stream().toList();
        return w.stream().sorted(Comparator.comparing(Word::getSentiment)).
                map(Word::getText).limit(n).toList();
    }

    @Override
    public boolean appendReview(String review, int sentiment) {
        if (review == null || review.isBlank() || review.isEmpty()) {
            throw new IllegalArgumentException("Review cannot be null, empty or blank");
        }
        if (sentiment < negative || sentiment > positive) {
            throw new IllegalArgumentException("Sentiment should be in the range [0.0, 4.0]");
        }

        getWords(review, sentiment);

        try (var out = new BufferedWriter(reviewsOut)) {
            out.write(sentiment + " " + review + System.lineSeparator());
            out.flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public int getSentimentDictionarySize() {
        return words.size();
    }

    @Override
    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }
}
