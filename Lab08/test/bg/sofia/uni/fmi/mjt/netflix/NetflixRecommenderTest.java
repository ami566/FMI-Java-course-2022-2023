package bg.sofia.uni.fmi.mjt.netflix;

import bg.sofia.uni.fmi.mjt.netflix.Content;
import bg.sofia.uni.fmi.mjt.netflix.ContentType;
import bg.sofia.uni.fmi.mjt.netflix.NetflixRecommender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class NetflixRecommenderTest {

    private static String movie1 = "tm191110,Titanic,MOVIE,101-year-old Rose DeWitt Bukater tells the story of her life aboard the Titanic; 84 years later. Rose tells the whole story from Titanic's departure through to its death—on its first and last voyage—on April 15; 1912.,1997,194,['drama'; 'romance'],-1,tt0120338,10.0,1146825.0";
    private static String movie2 = "tm95477,Troy,MOVIE,Paris; the Trojan prince; convinces Helen; Queen of Sparta; to leave her husband Menelaus; and sail with him back to Troy. After Menelaus finds out that his wife was taken by the Trojans; he asks his brother Agamemnom to help him get her back. Agamemnon sees this as an opportunity for power. So they set off with 1;000 ships holding 50;000 Greeks to Troy. With the help of Achilles; the Greeks are able to fight the never before defeated Trojans.,2004,163,['action'; 'history'; 'war'; 'drama'; 'european'],-1,tt0332452,7.3,527447.0";
    private static String movie3 = "tm154187,Sherlock Holmes,MOVIE,Eccentric consulting detective Sherlock Holmes and Doctor John Watson battle to bring down a new nemesis and unravel a deadly plot that could destroy England.,2009,129,['crime'; 'thriller'; 'action'],-1,tt0988045,7.6,625708.0";
    private static String show1 = "ts21247,The Vampire Diaries,SHOW,The story of two vampire brothers and their life; while being obsessed with the same girl; who bears a striking resemblance to the beautiful but ruthless vampire they knew and loved in 1864.,2009,42,['drama'; 'scifi'; 'horror'; 'romance'; 'thriller'; 'fantasy'],1,tt1405406,7.7,314422.0";
    private static String show2 = "ts12815,Big Time Rush,SHOW,Four teenage friends move from Minneapolis to Los Angeles to form a potential chart-topping boy band after Kendall is inadvertently discovered by an eccentric record executive; Gustavo Rocque. As they seize this opportunity of a lifetime; these friends embark on an exciting comedy and music-filled journey to prove to themselves and their record label that they are serious about their new career choice.,2009,25,['family'; 'comedy'],1,tt1131746,6.3,12288.0";
    private static String show3 = "ts36147,Lucifer,SHOW,Bored and unhappy as the Lord of Hell; Lucifer Morningstar abandoned his throne and retired to Los Angeles; where he has teamed up with LAPD detective Chloe Decker to take down criminals.But the longer he's away from the underworld; the greater the threat that the worst of humanity could escape.,2016,47,['scifi'; 'crime'; 'drama'; 'fantasy'],1,tt4052886,9.9,313124.0";
    private static List<Content> data;
    private static NetflixRecommender recommender;

    public static Reader initContent() {
        String[] content = {"", movie1, movie2, movie3, show1, show2, show3};
        return new StringReader(Arrays.stream(content).collect(Collectors.joining(System.lineSeparator())));
    }

    @BeforeAll
    public static void setUp() throws IOException {
        Reader contentStream = initContent();

        try (BufferedReader reader = new BufferedReader(contentStream)) {
            data = reader.lines().skip(1).map(Content::of).collect(Collectors.toList());
        }
        recommender = new NetflixRecommender(initContent());
    }

    @Test
    public void testIfExistingDatasetIsLoadedCorrectly() {
        assertEquals(data.size(), recommender.getAllContent().size(),
                "Number of movies and shows does not match the number of content in the dataset.");
    }

    @Test
    public void testGetTopNRatedMoviesAndShowsNegative() {
        assertThrows(IllegalArgumentException.class, () -> recommender.getTopNRatedContent(-2),
                "N cannot be negative");
    }

    @Test
    public void testGetTopNRatedContent() {
        List<Content> expected = new ArrayList<>() {
        };
        expected.add(Content.of(movie1));
        expected.add(Content.of(show3));

        assertArrayEquals(expected.toArray(), recommender.getTopNRatedContent(2).toArray(),
                "Top content should be in the expected order");
    }

    @Test
    public void testGetTheLongestMovie() {
        assertEquals(Content.of(movie1), recommender.getTheLongestMovie(),
                "Does not return the longest movie");
    }

    @Test
    public void testSearchContentByTwoKeyword() {
        List<Content> expected = new ArrayList<>() { };

        expected.add(Content.of(movie1));
        expected.add(Content.of(show1));
        List<Content> actual = recommender.getContentByKeywords("life", "story").stream().toList();
        assertArrayEquals(expected.toArray(), actual.toArray(),"Wrong answer");
    }

    @Test
    public void testGetAllGenres() {

        assertEquals(13, recommender.getAllGenres().size(), "Wrong count of genres");
    }

    @Test
    public void testSearchContentWithNoKeyword() {
        List<Content> expected = new ArrayList<>() {
        };

        assertArrayEquals(expected.toArray(), recommender.getContentByKeywords().toArray(),
                "Wrong answer");
    }

    @Test
    public void testSearchContentWithOneWrongKeyword() {
        List<Content> expected = new ArrayList<>() {
        };

        assertArrayEquals(expected.toArray(), recommender.getContentByKeywords("love").toArray(),
                "Wrong answer");
    }

    @Test
    public void testGetContentWithSimilarGenres() {
        Content testContent = Content.of(show1);
        List<Content> expected = new ArrayList<>() { };
        expected.add(testContent);
        expected.add(Content.of(show3));
        expected.add(Content.of(show2));
        assertArrayEquals(expected.toArray(), recommender.getSimilarContent(testContent).toArray(),
                "Not matching");
    }

    @Test
    public void testGroupContentByType() {
        Map<ContentType, Set<Content>> expected = Map.of(
                ContentType.MOVIE, Set.of(
                        Content.of(movie1),
                        Content.of(movie2),
                        Content.of(movie3)),
                ContentType.SHOW, Set.of(
                        Content.of(show1),
                        Content.of(show2),
                        Content.of(show3))
        );

        Map<ContentType, Set<Content>> actual = recommender.groupContentByType();

        assertEquals(expected.keySet(), actual.keySet(), "Wrong grouping by type");
        assertTrue(expected.entrySet().stream().allMatch(t ->
                        assertCollectionsEqual(t.getValue(), actual.get(t.getKey()))),
                "Wrong grouping by type");
    }

    private boolean assertCollectionsEqual(Collection<Content> expected, Collection<Content> actual) {
        return expected.containsAll(actual) && actual.containsAll(expected);
    }
}

