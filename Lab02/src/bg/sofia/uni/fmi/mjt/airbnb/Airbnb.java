package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;

public class Airbnb implements AirbnbAPI{
    Bookable[] accommodations;

    public Airbnb(Bookable[] accommodations){
        this.accommodations = accommodations;
    }

    public Bookable findAccommodationById(String id){
        if(id == null || id.equals(""))
            return null;

        for (Bookable b: accommodations ) {
            if(b.getId().equalsIgnoreCase(id))
                return b;
        }
        return null;
    }

    public  double estimateTotalRevenue() {
        double sum = 0;
        for(Bookable b: accommodations) {
            if (b.isBooked())
                sum += b.getTotalPriceOfStay();
        }
        return sum;
    }

    public long countBookings(){
        long count = 0;
        for(Bookable b: accommodations) {
            if (b.isBooked())
                count++;
        }
        return count;
    }


    public Bookable[] filterAccommodations(Criterion... criteria){
        if (criteria.length == 0){
            return accommodations;
        }

        Bookable[] newAccomm = new Bookable[accommodations.length];
        int j = 0;
        for (int i = 0; i < accommodations.length; i++) {
            boolean checks = true;
            for (Criterion c : criteria) {
                if (!c.check(accommodations[i])){
                    checks = false;
                }
            }

            if(checks){
                newAccomm[j++] = accommodations[i];
            }
        }
        Bookable[] filtered = new Bookable[j];

        for (int i = 0; i < j; i++){
            filtered[i] = newAccomm[i];
        }
        return filtered;
    }
}
