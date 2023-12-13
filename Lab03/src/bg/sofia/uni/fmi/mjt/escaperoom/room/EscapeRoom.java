package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class EscapeRoom implements Ratable {
    private String name;
    private Theme theme;
    private Difficulty difficulty;
    private int maxTimeToEscape;
    private int maxReviewsCount;
    private int sizeForA = 0;
    private int capacityForA = 16;
    private Achievement[] achievements;
    private double priceToPlay;
    private Review[] reviews;
    private int reviewsCount = 0;
    private double rating = 0.0;
    private int ratingsCount = 0;


    public EscapeRoom(String name, Theme theme, Difficulty difficulty, int maxTimeToEscape, double priceToPlay,
                      int maxReviewsCount){
        this.name = name;
        this.theme = theme;
        this.difficulty = difficulty;
        this.maxTimeToEscape = maxTimeToEscape;
        this.priceToPlay = priceToPlay;
        this.maxReviewsCount = maxReviewsCount;
        reviews = new Review[0];
        achievements = new Achievement[capacityForA];
    }

    public void addAchievement(Achievement a) {
        if(sizeForA == capacityForA){
            Achievement[] newArr = new Achievement[capacityForA *= 2];
            for (int i = 0; i < sizeForA; i++){
                newArr[i] = achievements[i];
            }

            achievements = newArr;
        }

        achievements[sizeForA++] = a;
    }
    public boolean CompareRoom(String r){
        return name.equals(r);
    }
    /**
     * Returns the name of the escape room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the difficulty of the escape room.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum time to escape the room.
     */
    public int getMaxTimeToEscape() {
        return maxTimeToEscape;
    }

    /**
     * Returns all user reviews stored for this escape room, in the order they have been added.
     */
    public Review[] getReviews() {
       return reviews;
    }

    /**
     * Adds a user review for this escape room.
     * The platform keeps just the latest up to {@code maxReviewsCount} reviews and in case the capacity is full,
     * a newly added review would overwrite the oldest added one, so the platform contains
     * {@code maxReviewsCount} at maximum, at any given time. Note that, despite older reviews may have been
     * overwritten, the rating of the room averages all submitted review ratings, regardless of whether all reviews
     * themselves are still stored in the platform.
     *
     * @param review the user review to add.
     */
    public void addReview(Review review) {
        if (reviewsCount != maxReviewsCount){
            Review[] newReviews = new Review[++reviewsCount];
            int i = 0;
            for (Review r: reviews){
                newReviews[i++] = r;
            }
            newReviews[i] = review;
            reviews = newReviews;
            rating += review.rating();
            ratingsCount++;
            return;
        }
        if (reviewsCount == maxReviewsCount){
            Review[] newRevArr = new Review[reviewsCount];
            for (int i = 0; i < reviewsCount - 1; i++){
                newRevArr[i] = reviews[i + 1];
            }
            newRevArr[reviewsCount - 1] = review;
            reviews = newRevArr;
        }

        rating += review.rating();
        ratingsCount++;
    }

    @Override
    public double getRating() {
        return ratingsCount == 0 ? 0.0 : rating / ratingsCount;
    }
}
