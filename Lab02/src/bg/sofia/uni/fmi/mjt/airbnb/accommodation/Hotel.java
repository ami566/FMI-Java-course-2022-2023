package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Hotel extends Accommodation{
    private static int count = 0;
    public Hotel(Location location, double pricePerNight){
        this.location = location;
        price = pricePerNight;
        ++count;
        id = "HOT-" + count;
    }

}
