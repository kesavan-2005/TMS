package com.village.repository;

import com.village.model.*;
import java.util.*;
import java.util.concurrent.*;

public class InMemoryRepository {
    private final ConcurrentMap<String, Vehicle> vehicles = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Passenger> passengers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Booking> bookings = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Route> routes = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<FareTransaction> transactions = new CopyOnWriteArrayList<>();

    // Vehicles
    public void saveVehicle(Vehicle v) { vehicles.put(v.getId(), v); }
    public Optional<Vehicle> findVehicle(String id) { return Optional.ofNullable(vehicles.get(id)); }
    public Collection<Vehicle> findAllVehicles() { return vehicles.values(); }
    public void deleteVehicle(String id) { vehicles.remove(id); }

    // Passengers
    public void savePassenger(Passenger p) { passengers.put(p.id(), p); }
    public Optional<Passenger> findPassenger(String id) { return Optional.ofNullable(passengers.get(id)); }
    public Collection<Passenger> findAllPassengers() { return passengers.values(); }

    // Routes
    public void saveRoute(Route r) { routes.put(r.getId(), r); }
    public Optional<Route> findRoute(String id) { return Optional.ofNullable(routes.get(id)); }
    public Collection<Route> findAllRoutes() { return routes.values(); }

    // Bookings
    public void saveBooking(Booking b) { bookings.put(b.getId(), b); }
    public Optional<Booking> findBooking(String id) { return Optional.ofNullable(bookings.get(id)); }
    public Collection<Booking> findAllBookings() { return bookings.values(); }
    public void deleteBooking(String id) { bookings.remove(id); }

    // Transactions
    public void addTransaction(FareTransaction t) { transactions.add(t); }
    public List<FareTransaction> findAllTransactions() { return new ArrayList<>(transactions); }
}
