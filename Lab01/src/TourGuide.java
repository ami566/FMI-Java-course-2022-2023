public class TourGuide {
    public static int getBestSightseeingPairScore(int[] places){
        int bestRating = 0;
        for (int i = 0; i < places.length - 1; i++){
            for (int j = i + 1; j < places.length; j++){
                int rating = places[i] + places[j] + i - j;
                if(rating > bestRating)
                    bestRating = rating;
            }
        }
        return bestRating;
    }

    public static void main(String[] args){
        System.out.println(getBestSightseeingPairScore(new int[]{1, 2}));
    }
}
