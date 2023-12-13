package bg.sofia.uni.fmi.mjt.netflix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NetflixRecommender {

    private List<Content> data;

    /**
     * Loads the dataset from the given {@code reader}.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public NetflixRecommender(Reader reader) {
        data = new ArrayList<>();
        try (var r = new BufferedReader(reader)) {
            data = r.lines().skip(1).map(Content::of).toList();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from the file", e);
        }
    }

    /**
     * Returns all movies and shows from the dataset in undefined order as an unmodifiable List.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all movies and shows.
     */
    public List<Content> getAllContent() {
        return Collections.unmodifiableList(data);
    }

    /**
     * Returns a list of all unique genres of movies and shows in the dataset in undefined order.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all genres
     */
    public List<String> getAllGenres() {
        if (data.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> set = new HashSet<>();

        for (Content c : data) {
            for (String s : c.genres()) {
                set.add(s);
            }
        }

        return set.stream().toList();
    }

    /**
     * Returns the movie with the longest duration / run time. If there are two or more movies
     * with equal maximum run time, returns any of them. Shows in the dataset are not considered by this method.
     *
     * @return the movie with the longest run time
     * @throws NoSuchElementException in case there are no movies in the dataset.
     */
    public Content getTheLongestMovie() {
        return data.stream()
                .filter(el -> el.type() == ContentType.MOVIE)
                .max(Comparator.comparingInt(Content::runtime))
                .orElseThrow(() -> new NoSuchElementException("There are no movies in the dataset"));

    }

    /**
     * Returns a breakdown of content by type (movie or show).
     *
     * @return a Map with key: a ContentType and value: the set of movies or shows on the dataset, in undefined order.
     */
    public Map<ContentType, Set<Content>> groupContenStByType() {
        return data.stream()
                .collect(Collectors.groupingBy(Content::type,
                        Collectors.mapping(Function.identity(),
                                Collectors.toSet())));
    }

    private Double avgScore() {
        Double sum = data.stream().mapToDouble(Content::imdbScore).sum();
        return sum / data.size();
    }

    /**
     * Returns the top N movies and shows sorted by weighed IMDB rating in descending order.
     * If there are fewer movies and shows than {@code n} in the dataset, return all of them.
     * If {@code n} is zero, returns an empty list.
     * <p>
     * The weighed rating is calculated by the following formula:
     * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
     * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.
     * <p>
     * Check https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating for details.
     *
     * @param n the number of the top-rated movies and shows to return
     * @return the list of the top-rated movies and shows
     * @throws IllegalArgumentException if {@code n} is negative.
     */
    public List<Content> getTopNRatedContent(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N cannot be negative");
        }

        final int m = 10000;

        return data.stream().sorted((c1, c2) -> Double.compare(
                (c2.imdbVotes() / (c2.imdbVotes() + m)) * c2.imdbScore() +
                        (m / (c2.imdbVotes() + m)) * avgScore(),
                (c1.imdbVotes() / (c1.imdbVotes() + m)) * c1.imdbScore() +
                        (m / (c1.imdbVotes() + m)) * avgScore())).limit(n).toList();
    }

    /**
     * Returns a list of content similar to the specified one sorted by similarity is descending order.
     * Two contents are considered similar, only if they are of the same type (movie or show).
     * The used measure of similarity is the number of genres two contents share.
     * If two contents have equal number of common genres with the specified one, their mutual oder
     * in the result is undefined.
     *
     * @param content the specified movie or show.
     * @return the sorted list of content similar to the specified one.
     */
    public List<Content> getSimilarContent(Content content) {
        List<Content> filtered = data.stream().filter(el -> el.type() == content.type()).toList();

        Map<Integer, List<Content>> similarities = new HashMap<>();

        for (Content c : filtered) {

            int matches = (int) c.genres().stream().filter(content.genres()::contains).count();
            if (similarities.containsKey(matches)) {
                similarities.get(matches).add(c);
            } else {
                List<Content> l = new ArrayList<>();
                l.add(c);
                similarities.put(matches, l);
            }
        }

        List<Content> result = new ArrayList<>();
        for (int g : similarities.keySet()) {
            result.addAll(similarities.get(g));
        }

        Collections.reverse(result);
        return result;
    }

    /**
     * Searches content by keywords in the description (case-insensitive).
     *
     * @param keywords the keywords to search for
     * @return an unmodifiable set of movies and shows whose description contains all specified keywords.
     */
    public Set<Content> getContentByKeywords(String... keywords) {
        if (keywords.length == 0) {
            return new HashSet<>();
        }

        Set<Content> set = new HashSet<>();

        List<String> keys = Arrays.asList(keywords).stream().map(k -> ".*?\\b" + k.toLowerCase() + "\\b.*?").toList();

        for (Content c : data) {
            boolean contains = true;
            for (String s : keys) {
                if (!c.description().matches(s)) {
                    contains = false;
                }
            }
            if (contains) {
                set.add(c);
            }
        }
        return Collections.unmodifiableSet(set);
    }

}