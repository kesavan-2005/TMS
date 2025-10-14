package com.village.service;

import com.village.model.*;
import com.village.repository.InMemoryRepository;

import java.util.Collection;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {
    private final InMemoryRepository repo;

    public BookingServiceImpl(InMemoryRepository repo) { this.repo = repo; }

    @Override
    public Booking createBooking(String vehicleId, String passengerId, String routeId) {
        Optional<Vehicle> v = repo.findVehicle(vehicleId);
        Optional<Passenger> p = repo.findPassenger(passengerId);
        Optional<Route> r = repo.findRoute(routeId);
        if (v.isEmpty()) throw new BookingException("Vehicle not found");
        if (p.isEmpty()) throw new BookingException("Passenger not found");
        if (r.isEmpty()) throw new BookingException("Route not found");
        Booking b = new Booking(vehicleId, passengerId, routeId);
        b.setStatus(BookingStatus.CONFIRMED);
        repo.saveBooking(b);
        return b;
    }

    @Override
    public Collection<Booking> listBookings() { return repo.findAllBookings(); }

    @Override
    public void cancelBooking(String bookingId) {
        Optional<Booking> ob = repo.findBooking(bookingId);
        if (ob.isEmpty()) throw new BookingException("Booking not found");
        Booking b = ob.get();
        b.setStatus(BookingStatus.CANCELLED);
        repo.saveBooking(b);
    }

    @Override
    public Passenger registerPassenger(String name, String phone) {
        String id = java.util.UUID.randomUUID().toString();
        Passenger p = new Passenger(id, name, phone);
        repo.savePassenger(p);
        return p;
    }

    @Override
    public Collection<Passenger> listPassengers() { return repo.findAllPassengers(); }

    @Override
    public Route addRoute(String name, double distanceKm) {
        SimpleRoute r = new SimpleRoute(name, distanceKm);
        repo.saveRoute(r);
        return r;
    }

    @Override
    public Collection<Route> listRoutes() { return repo.findAllRoutes(); }
}
