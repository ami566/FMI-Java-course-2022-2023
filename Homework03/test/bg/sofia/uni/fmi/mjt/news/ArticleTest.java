package bg.sofia.uni.fmi.mjt.news;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    void testPrint() {
        Article article1 = new Article(null, "a", "b", "v", "g", "d", "e", "j");
        assertEquals("", article1.toString());
    }
}