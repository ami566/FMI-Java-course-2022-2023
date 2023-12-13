package bg.sofia.uni.fmi.mjt.flightscanner.exception;

public class FlightCapacityExceededException extends RuntimeException {
    public FlightCapacityExceededException(String message) {
        super(message);
    }
}
