package bg.sofia.uni.fmi.mjt.flightscanner;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;

import java.util.*;

public class FlightScanner implements FlightScannerAPI {
    private HashMap<Airport, List<Flight>> airportAndFlightsFrom;

    public FlightScanner() {
        airportAndFlightsFrom = new HashMap<Airport, List<Flight>>();
    }

    @Override
    public void add(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }

        if (airportAndFlightsFrom.containsKey(flight.getFrom())) {
            airportAndFlightsFrom.get(flight.getFrom()).add(flight);
            return;
        }
        List<Flight> fl = new ArrayList<>();
        fl.add(flight);
        airportAndFlightsFrom.put(flight.getFrom(), fl);
    }

    @Override
    public void addAll(Collection<Flight> flights) {
        if (flights == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }
        for (Flight f : flights) {
            add(f);
        }
    }

    private List<Flight> bfs(Airport from, Airport to) {
        Stack<List<Flight>> stack = new Stack<>();
        stack.add(airportAndFlightsFrom.get(from));

        Set<Airport> visited = new HashSet<Airport>();
        visited.add(from);

        List<Flight> prev = new ArrayList<>();

        while (!stack.isEmpty() && !visited.contains(to)) {
            Collection<Flight> flights = stack.peek();
            stack.pop();

            for (Flight f : flights) {
                if (!visited.contains(f.getTo())) {
                    stack.add(airportAndFlightsFrom.get(f.getTo()));
                    visited.add(f.getTo());
                    prev.add(f);
                }
            }
        }
        return prev;
    }

    private List<Flight> reconstructPath(Airport from, Airport to, List<Flight> path) {
        Stack<Flight> res = new Stack<>();

        int i = path.size() - 1;
        while (i >= 0 && path.get(i).getTo() != to) {
            i--;
        }
        if (i < 0) {
            return res.stream().toList();
        }
        if (path.get(i).getTo() == to) {
            res.add(path.get(i));
            Flight f = path.get(i--);
            for (; i >= 0 && f != null; i--) {
                if (f.getFrom() == path.get(i).getTo()) {
                    res.add(path.get(i));
                    f = path.get(i);
                }
                if (f.getFrom() == from) {
                    break;
                }
            }

            if (f != null) {
                return res.stream().toList();
            }
        }
        return res.stream().toList();
    }

    @Override
    public List<Flight> searchFlights(Airport from, Airport to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Airport cannot be null");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("From and To airports should be different");
        }

        return reconstructPath(from, to, bfs(from, to));

//        if (!airportAndFlightsFrom.containsKey(from)) {
//            return new ArrayList<>();
//        }
//
//        boolean connected = false;
//
//        List<Airport> visited = new ArrayList<>();
//        Queue<Airport> q = new LinkedList<>();
//
//        q.add(from);
//        visited.add(from);
//
//        while (!q.isEmpty()) {
//            Airport nextVertex = q.poll();
//
//            if (nextVertex.equals(to)) {
//                connected = true;
//                break;
//            }
//
//            for (Flight edge : airportAndFlightsFrom.get(nextVertex)) {
//
//                if (!visited.contains(edge.getTo())) {
//                    q.add(edge.getTo());
//                    visited.add(edge.getTo());
//                }
//            }
//        }
//
//        if (!connected || visited == null || visited.size() == 0) {
//            return new ArrayList<>();
//        }
//
//        if (visited.size() == 1) {
//            return new ArrayList<>();
//        }
//
//        List<Flight> fl = new ArrayList<>();
//
//        for (int i = 0; i < visited.size() - 1; i++) {
//            fl.add(findFlight(visited.get(i), visited.get(i + 1)));
//        }
//        if (fl.size() == 1) {
//            return fl;
//        }
//        return new ArrayList<>();
    }

    private Flight findFlight(Airport from, Airport to) {
        List<Flight> fl = airportAndFlightsFrom.get(from);
        if (fl == null) {
            return null;
        }
        for (Flight f : fl) {
            if (f.getTo().equals(to)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {
        if (from == null) {
            throw new IllegalArgumentException("Airport cannot be null");
        }
        List<Flight> fl = new ArrayList<>();
        if (!airportAndFlightsFrom.containsKey(from)) {
            return fl;
        }
        fl = airportAndFlightsFrom.get(from);
        if (fl != null) {
            Collections.sort(fl, new FreeSeatsComparator());
            Collections.reverse(fl);
        }
        return fl;
    }

    @Override
    public List<Flight> getFlightsSortedByDestination(Airport from) {
        if (from == null) {
            throw new IllegalArgumentException("Airport cannot be null");
        }
        List<Flight> fl = new ArrayList<>();
        if (!airportAndFlightsFrom.containsKey(from)) {
            return fl;
        }
        fl = airportAndFlightsFrom.get(from);
        if (fl != null) {
            Collections.sort(fl, new LexicographicComparatorForFlights());
        }
        return fl;
    }
}

class LexicographicComparatorForFlights implements Comparator<Flight> {
    @Override
    public int compare(Flight a, Flight b) {
        return a.getTo().ID().compareToIgnoreCase(b.getTo().ID());
    }
}

class FreeSeatsComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight a, Flight b) {
        return Integer.compare(a.getFreeSeatsCount(), b.getFreeSeatsCount());
    }
}