package com.village.service;

import com.village.model.Booking;
import com.village.model.Passenger;
import com.village.model.Route;

import java.util.Collection;

public interface BookingService {
    Booking createBooking(String vehicleId, String passengerId, String routeId);
    Collection<Booking> listBookings();
    void cancelBooking(String bookingId);
    Passenger registerPassenger(String name, String phone);
    Collection<Passenger> listPassengers();
    Route addRoute(String name, double distanceKm);
    Collection<Route> listRoutes();
}
