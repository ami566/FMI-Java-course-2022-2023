package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class LocationCriterion implements Criterion {
    Location currLoc;
    double maxDist;

    public LocationCriterion(Location currentLocation, double maxDistance){
        currLoc = currentLocation;
        maxDist = maxDistance;
    }

    public boolean check(Bookable bookable){
        if(bookable == null) {
            return false;
        }
        return Math.sqrt((bookable.getLocation().y - currLoc.y) * (bookable.getLocation().y - currLoc.y) + (bookable.getLocation().x - currLoc.x) * (bookable.getLocation().x - currLoc.x)) <= maxDist;
    }
}
