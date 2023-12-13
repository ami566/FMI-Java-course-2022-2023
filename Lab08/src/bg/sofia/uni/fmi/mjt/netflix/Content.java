package bg.sofia.uni.fmi.mjt.netflix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Content(String id, String title, ContentType type, String description, int releaseYear, int runtime,
                      List<String> genres, int seasons, String imdbId, double imdbScore, double imdbVotes) {

    private static final int ID = 0;
    private static final int TITLE = 1;
    private static final int CONTENT_TYPE = 2;
    private static final int DESCRIPTION = 3;
    private static final int RELEASE_YEAR = 4;
    private static final int RUNTIME = 5;
    private static final int GENRES = 6;
    private static final int SEASONS = 7;
    private static final int IMDB_ID = 8;
    private static final int IMDB_SCORE = 9;
    private static final int IMDB_VOTES = 10;

    public static Content of(String line) {
        String[] tokens = line.split(",");

        String id = tokens[ID];
        String title = tokens[TITLE];
        ContentType type = tokens[CONTENT_TYPE].equals("MOVIE") ? ContentType.MOVIE : ContentType.SHOW;
        String description = tokens[DESCRIPTION];
        int releaseYear = Integer.parseInt(tokens[RELEASE_YEAR]);
        int runtime = Integer.parseInt(tokens[RUNTIME]);
        List<String> genres = Stream.of(tokens[GENRES].split(";"))
                .map(s -> s.replace("[", "").replace("]", "").replaceAll("'", ""))
                .map(s -> s.trim())
                .collect(Collectors.toList());
        int seasons = Integer.parseInt(tokens[SEASONS]);
        String imdbId = tokens[IMDB_ID];
        double imdbScore = Double.parseDouble(tokens[IMDB_SCORE]);
        double imdbVotes = Double.parseDouble(tokens[IMDB_VOTES]);

        return new Content(id, title, type, description, releaseYear, runtime,
                genres, seasons, imdbId, imdbScore, imdbVotes);

    }
}
