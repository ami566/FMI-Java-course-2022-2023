package bg.sofia.uni.fmi.mjt.escaperoom.room;

public record Review(int rating, String reviewText) {
    public Review {
        if(rating < 0 || rating > 10){
            throw new IllegalArgumentException("Rating cannot be 0, negative or over 10");
        }

        if(reviewText == null || reviewText.length() > 200){
            throw new IllegalArgumentException("Review text cannot be empty or longer than 200 symbols");
        }
    }
}
