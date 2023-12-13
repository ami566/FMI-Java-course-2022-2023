package bg.sofia.uni.fmi.mjt.markdown.tags;

public enum Tag {
    HTML_TAG("<html>"),
    BODY_TAG("<body>"),
    CODE_TAG("<code>"),
    BOLD_TAG("<strong>"),
    EM_TAG("<em>"),
    H1_TAG("<h1>"),
    H2_TAG("<h2>"),
    H3_TAG("<h3>"),
    H4_TAG("<h4>"),
    H5_TAG("<h5>"),
    H6_TAG("<h6>"),
    HTML_CLOSING_TAG("</html>"),
    BODY_CLOSING_TAG("</body>"),
    CODE_CLOSING_TAG("</code>"),
    BOLD_CLOSING_TAG("</strong>"),
    EM_CLOSING_TAG("</em>"),
    H1_CLOSING_TAG("</h1>"),
    H2_CLOSING_TAG("</h2>"),
    H3_CLOSING_TAG("</h3>"),
    H4_CLOSING_TAG("</h4>"),
    H5_CLOSING_TAG("</h5>"),
    H6_CLOSING_TAG("</h6>");

    public final String text;

    Tag(String text) {
        this.text = text;
    }
}
