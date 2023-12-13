package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RegularFlight implements Flight {
    private String flightId;
    private Airport from;
    private Airport to;
    private int totalCapacity;
    private List<Passenger> passengers;

    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.totalCapacity = totalCapacity;
        passengers = new ArrayList<>();
    }

    public static RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity) {
        if (flightId == null || flightId.isBlank() || flightId.isEmpty()) {
            throw new IllegalArgumentException("Flight ID cannot be null, empty or blank");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Airport cannot be null");
        }
        if (totalCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative number");
        }
        if (from.equals(to)) {
            throw new InvalidFlightException("From and To airports should be different");
        }
        return new RegularFlight(flightId, from, to, totalCapacity);
    }

    @Override
    public Airport getFrom() {
        return from;
    }

    @Override
    public Airport getTo() {
        return to;
    }

    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {
        if (passengers.size() == totalCapacity) {
            throw new FlightCapacityExceededException("The capacity of the flight has already been exceeded");
        }

        passengers.add(passenger);
    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {
        if (this.passengers == null || this.passengers.size() + passengers.size() > totalCapacity) {
            throw new FlightCapacityExceededException("The capacity of the flight has already been exceeded");
        }
        this.passengers.addAll(passengers);
    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        if (passengers.size() == 0) {
            return List.of();
        }
        return Collections.unmodifiableList(passengers);
    }

    @Override
    public int getFreeSeatsCount() {
        return totalCapacity - passengers.size();
    }
}
