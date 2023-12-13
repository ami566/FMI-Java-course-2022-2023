package bg.sofia.uni.fmi.mjt.news;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchWords {
    private List<String> keywords;
    private String category;
    private String country;

    public static SearchWords of(String keywords, String category, String country) {
       return new SearchWords(keywords, category, country);
    }

    public SearchWords(String keywords, String category, String country) {
        setCategory(category);
        setCountry(country);
        setKeywords(keywords);
    }
    public SearchWords() {
        keywords = new ArrayList<>();
    }

    public void setCategory(String c) {
        category = c;
    }

    public void setCountry(String c) {
        country = c;
    }

    public void setKeywords(String words) {
        keywords = Arrays.stream(words.split(" ")).toList();
    }

    public List<String> Keywords() {
        return List.copyOf(keywords);
    }

    public String Country() {
        return country;
    }

    public String Category() {
        return category;
    }
}
