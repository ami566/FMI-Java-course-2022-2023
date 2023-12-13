package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Apartment extends Accommodation {
    private static int count = 0;
    public Apartment(Location location, double pricePerNight){
        this.location = location;
        price = pricePerNight;
        ++count;
        id = "APA-" + count;
    }
}
