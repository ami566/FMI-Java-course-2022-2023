package bg.sofia.uni.fmi.mjt.news.server;

import bg.sofia.uni.fmi.mjt.news.Article;

import java.util.List;

public class Response {
    private String status;
    private String totalResults;
    private List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
