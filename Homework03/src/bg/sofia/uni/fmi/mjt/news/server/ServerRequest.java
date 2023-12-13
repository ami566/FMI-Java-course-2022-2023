package bg.sofia.uni.fmi.mjt.news.server;

import bg.sofia.uni.fmi.mjt.news.Article;
import bg.sofia.uni.fmi.mjt.news.SearchWords;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ServerRequest {
    private final int maxPageSize = 50;
    private final String queryURL = "https://newsapi.org/v2/top-headlines?";
    private final String apiKey = "5ab59b1f641248e9a1922e2ac6e58015";
    private Gson gson;
    private final HttpClient client;

    public ServerRequest() {
        client = HttpClient.newBuilder().build();
        gson = new Gson();
    }

    private String generateQuery(SearchWords sw, int pageNumber) {
        StringBuilder query = new StringBuilder(queryURL);
        List<String> keywords = sw.Keywords();
        query.append("q=" + keywords.get(0));
        for (int i = 1; i < keywords.size(); i++) {
            query.append("+" + keywords.get(i));
        }

        if (sw.Country() != null) {
            query.append("&country=" + sw.Country());
        }

        if (sw.Category() != null) {
            query.append("&category=" + sw.Category());
        }

        query.append("&page=" + pageNumber);
        query.append("&pageSize=" + maxPageSize);
        query.append("&apiKey=" + apiKey);

        return query.toString();
    }

    public List<Article> getArticles(SearchWords sw, int pageNumber) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(generateQuery(sw, pageNumber))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Response responseString = gson.fromJson(response.body(), Response.class);

        return responseString.getArticles();
    }


}
