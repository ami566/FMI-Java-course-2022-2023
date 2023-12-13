package bg.sofia.uni.fmi.mjt.news;

public record Article(ArticleSource source, String author, String title, String description, String urlToImage,
                      String url,
                      String publishedAt, String content) {

    public void print() {
        String s = "Source: \n\t id: " + source.id() + "\n\t name: " + source.name()
                + "\nAuthor: " + author
                + "\nTitle: " + title
                + "\nDescription: " + description
                + "\nURLToImage: " + urlToImage
                + "\nURL: " + url
                + "\npublished at: " + publishedAt
                + "\nContent: " + content;

        System.out.println(s);
    }
}
