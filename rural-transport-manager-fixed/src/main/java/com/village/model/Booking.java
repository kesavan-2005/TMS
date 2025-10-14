package com.village.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Booking implements java.io.Serializable {
    private final String id;
    private final String vehicleId;
    private final String passengerId;
    private final String routeId;
    private BookingStatus status;
    private final LocalDateTime createdAt;

    public Booking(String vehicleId, String passengerId, String routeId) {
        this.id = UUID.randomUUID().toString();
        this.vehicleId = vehicleId;
        this.passengerId = passengerId;
        this.routeId = routeId;
        this.status = BookingStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getVehicleId() { return vehicleId; }
    public String getPassengerId() { return passengerId; }
    public String getRouteId() { return routeId; }
    public BookingStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(BookingStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Booking{" + id + ", vehicle=" + vehicleId + ", passenger=" + passengerId + ", route=" + routeId + ", status=" + status + "}";
    }
}
