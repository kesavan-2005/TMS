package com.village.model;

public class Vehicle extends Transport {
    private VehicleType type;

    public Vehicle(String id, String driverName, String plateNumber, int capacity, VehicleType type) {
        super(id, driverName, plateNumber, capacity);
        this.type = type;
    }

    public VehicleType getVehicleType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type.name();
    }

    @Override
    public String toString() {
        return String.format("Vehicle[id=%s,type=%s,driver=%s,plate=%s,cap=%d]",
                id, type, driverName, plateNumber, capacity);
    }
}
