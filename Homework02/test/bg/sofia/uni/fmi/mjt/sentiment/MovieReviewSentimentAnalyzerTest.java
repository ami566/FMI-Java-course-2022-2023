package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieReviewSentimentAnalyzerTest {
    MovieReviewSentimentAnalyzer analyzer;
    Writer reviewsOut;
    Reader reviewsIn;
    Reader stopWordsIn;
    String reviews = "3 In fact , even better.\t\n" +
            "3 Genuinely unnerving , better .\t\n" +
            "4 Except it's much , much better better tells .\t\n" +
            "4 Tells a fascinating , compelling story .\t\n" +
            "0 Feels tells except ... compelling strangely hollow and flat at its emotional core .\t\n" +
            "0 Feels strangely hollow and flat at its emotional core .\t\n" +
            "0 The jokes are flat , and the action looks fake .\t\n" +
            "1 You could hate it for the same reason .\t\n" +
            "2 There are many jokes about putting the toilet seat down .\t\n" +
            "2 An uplifting , largely bogus story .\t\n" +
            "2 Not as good as the original , but what is ...\t\n" +
            "1 good , better what\t\n" +
            "4 BETTER\t\n" +
            "2 Fortunately , you still have that option .\t";
    String stopWords = "in\nit's\na\nat\nits\nthe\nare\nand\nyou\ncould\n" +
            "for\nsame\nthere\nmany\nabout\ndown\nan\nnot\nas\nbut\nwhat\nis\nhave\nthat";

    @BeforeEach
    public void setUp() {
        reviewsIn = new StringReader(reviews);
        stopWordsIn = new StringReader(stopWords);
        reviewsOut = new StringWriter();
        analyzer = new MovieReviewSentimentAnalyzer(stopWordsIn, reviewsIn, reviewsOut);
    }

    @Test
    public void testGetDictionarySize() {
        assertEquals(35, analyzer.getSentimentDictionarySize(),
                "Does not include all words or there are missing words");
    }

    @Test
    public void testIsStopWordTrue() {
        assertTrue(analyzer.isStopWord("what"),
                "Does not recognize stop word");
    }

    @Test
    public void testIsStopWordWrong() {
        assertFalse(analyzer.isStopWord("hello"),
                "Does not recognize false stop word");
    }

    @Test
    public void testAppendReviewInvalidStringParams() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview("", 2),
                "Review cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview("   ", 3),
                "Review cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview(null, 4),
                "Review cannot be null");
    }

    @Test
    public void testAppendReviewInvalidSentimentParams() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview("hello", -6),
                "Sentiment cannot be negative");
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview("hello", 6),
                "Sentiment cannot be greater than 4");
    }

    @Test
    public void testAppendReviewCorrectData() {
        assertTrue(analyzer.appendReview("hello", 3),
                "Appending review does not work");
    }

    @Test
    public void testGetWordFrequencyStopWord() {
        assertEquals(-1, analyzer.getWordFrequency("what"),
                "Should return undefined value for stopwords");
    }

    @Test
    public void testGetWordFrequencyNotInDictionary() {
        assertEquals(0, analyzer.getWordFrequency("love"),
                "Should return 0 for unknown words");
    }

    @Test
    public void testGetWordFrequencyCorrect() {
        assertEquals(6, analyzer.getWordFrequency("better"),
                "Should return correct value for known words");
    }

    @Test
    public void testGetWordSentimentUnknown() {
        assertEquals(-1, analyzer.getWordSentiment("love"),
                "Should return -1 for unknown words");
    }

    @Test
    public void testGetWordSentimentCorrectWithTwoAppearances() {
        assertEquals(1, analyzer.getWordSentiment("jokes"),
                "Should return correct sentiment");
    }

    @Test
    public void testGetWordSentimentCorrectWithOneAppearance() {
        assertEquals(3, analyzer.getWordSentiment("genuinely"),
                "Should return correct sentiment");
    }

    @Test
    public void testGetReviewSentimentCorrectWithOneWordPresent() {
        String rev = "I like the jokes";
        assertEquals(1, analyzer.getReviewSentiment(rev),
                "Should return correct sentiment");
    }

    @Test
    public void testGetReviewSentimentUnknown() {
        String rev = "I loved the movie";
        assertEquals(-1, analyzer.getReviewSentiment(rev),
                "Should return correct sentiment");
    }

    @Test
    public void testGetReviewSentimentCorrectWithMultipleWords() {
        String rev = "The story is not compelling but I like the original jokes about the toilet";
        assertEquals(2.0, analyzer.getReviewSentiment(rev),
                "Should return correct sentiment");
    }

    @Test
    public void testGetReviewSentimentAsNameUnknown() {
        String rev = "I loved the movie";
        assertEquals("unknown", analyzer.getReviewSentimentAsName(rev),
                "Should return correct sentiment text");
    }

    @Test
    public void testGetReviewSentimentAsNameNeutral() {
        String rev = "The story is not compelling but I like the original jokes about the toilet"; // 2.4
        assertEquals("neutral", analyzer.getReviewSentimentAsName(rev),
                "Should return correct sentiment text");
    }

    @Test
    public void testGetReviewSentimentAsNameSomewhatPositive() {
        String rev = "Fascinating story , loved the seat interview"; // 2.667
        assertEquals("somewhat positive", analyzer.getReviewSentimentAsName(rev),
                "Should return correct sentiment text");
    }

    @Test
    public void testGetReviewSentimentAsNameSomewhatNegative() {
        String rev = "Hate the jokes"; // 1
        assertEquals("somewhat negative", analyzer.getReviewSentimentAsName(rev),
                "Should return correct sentiment text");
    }

    @Test
    public void testGetReviewSentimentAsNameNegative() {
        String rev = "Fake, EmoTional neWs"; // 0
        assertEquals("negative", analyzer.getReviewSentimentAsName(rev),
                "Should return correct sentiment text");
    }

    @Test
    public void testGetReviewSentimentAsNamePositive() {
        String rev = "Fascinating romantic plot"; // 4
        assertEquals("positive", analyzer.getReviewSentimentAsName(rev),
                "Should return correct sentiment text");
    }

    @Test
    public void testGetMostFrequentWordsNegativeParam() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getMostFrequentWords(-6),
                "N cannot be negative");
    }

    @Test
    public void testGetMostPositiveWordsNegativeParam() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getMostPositiveWords(-9),
                "N cannot be negative");
    }

    @Test
    public void testGetMostNegativeWordsNegativeParam() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getMostNegativeWords(-9),
                "N cannot be negative");
    }

    @Test
    public void testGetMostFrequentWords() {
        List<String> s = new ArrayList<>();
        s.add("better");
        List<String> res = analyzer.getMostFrequentWords(1);
        assertArrayEquals(s.toArray(), res.toArray(),
                "Wrong most frequent words");
    }

    @Test
    public void testGetMostFrequentWordsN2() {
        List<String> s = new ArrayList<>();
        s.add("better");
        s.add("flat");
        List<String> res = analyzer.getMostFrequentWords(2);
        assertArrayEquals(s.toArray(), res.toArray(),
                "Wrong most frequent words");
    }

    @Test
    public void testGetMostPositiveWordsWithOneWord() {
        List<String> s = new ArrayList<>();
        s.add("fascinating");
        List<String> res = analyzer.getMostPositiveWords(1);
        assertArrayEquals(s.toArray(), res.toArray(),
                "Wrong most positive words");
    }

    @Test
    public void testGetMostNegativeWordsWithMoreThanPresentWords() {
        List<String> res = analyzer.getMostNegativeWords(36);
        assertEquals(35, res.size(),
                "For given more than present words, has to return all of the present ones sorted");
    }

    @Test
    public void testGetMostPositiveWordsWithMoreThanPresentWords() {
        List<String> res = analyzer.getMostPositiveWords(60);
        assertEquals(35, res.size(),
                "For given more than present words, has to return all of the present ones sorted");
    }

    @Test
    public void testGetMostFrequentWordsWithMoreThanPresentWords() {
        List<String> res = analyzer.getMostFrequentWords(75);
        assertEquals(35, res.size(),
                "For given more than present words, has to return all of the present ones sorted");
    }

}
