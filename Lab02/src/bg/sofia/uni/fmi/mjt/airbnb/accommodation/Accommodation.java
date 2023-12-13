package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Accommodation implements Bookable{
    String id;
    double price;
    Location location;
    boolean booked;
    long nightsBooked;
    public String getId(){
        return id;
    }

    public Location getLocation(){
        return location;
    }

    public boolean isBooked(){
        return booked;
    }

    public boolean book(LocalDateTime checkIn, LocalDateTime checkOut){
        if(booked || checkIn == null || checkOut == null || checkIn.isBefore(LocalDateTime.now()) || checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)){
            return false;
        }
        nightsBooked = Duration.between(checkIn, checkOut).toDays();
        booked = true;
        return true;
    }

    public double getTotalPriceOfStay(){
        if(booked){
            return price * nightsBooked;
        }
        else return 0.0;
    }

    public double getPricePerNight(){
        return price;
    }
}
