package bg.sofia.uni.fmi.mjt.news.client;

import bg.sofia.uni.fmi.mjt.news.Article;
import bg.sofia.uni.fmi.mjt.news.SearchWords;
import bg.sofia.uni.fmi.mjt.news.server.ServerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientInterfaceTest {
    @Mock
    ServerRequest serverRequest;
    SearchWords sw = SearchWords.of("cat lovely", "cats", "Bulgaria");

    Article article1 = new Article(null, "Amira", "Cats in Bulgaria", "the cats",
            "url", "https://nkddf", "Sunday 22.1.2023", "There are a lot of lovely and not so lovely cats in Bulgaria.");
    Article article2 = new Article(null, "Amira", "Student City Sofia", "the cats",
            "url", "https://nkddf", "Sunday 22.1.2023", "There are a lot of lovely and not so lovely cats in front of my dormitory.");
    Article article3 = new Article(null, "Amira", "FMI", "the cats",
            "url", "https://nkddf", "Sunday 22.1.2023", "There are a lot of lovely and not so lovely cats in front of FMI.");


    ClientInterface clientInterface;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        serverRequest = mock(ServerRequest.class);
        clientInterface = new ClientInterface(serverRequest);
    }

    @Test
    void testHandleCommandInvalidCommand() {
        assertFalse(clientInterface.handleCommand("9"));
    }

    @Test
    void testHandleCommand2() throws IOException, InterruptedException {
        when(serverRequest.getArticles(sw, 2)).thenReturn(List.of(article1, article2, article3));
        clientInterface.setSearchWords(sw);
        clientInterface.loadArticles(2);
        assertEquals(3, clientInterface.Articles().size(), "");
    }

}
