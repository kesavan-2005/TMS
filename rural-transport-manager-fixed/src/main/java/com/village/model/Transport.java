package com.village.model;

import java.io.Serializable;

public abstract class Transport implements Serializable {
    protected final String id;
    protected String driverName;
    protected String plateNumber;
    protected int capacity;

    public Transport(String id, String driverName, String plateNumber, int capacity) {
        this.id = id;
        this.driverName = driverName;
        this.plateNumber = plateNumber;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public String getDriverName() { return driverName; }
    public String getPlateNumber() { return plateNumber; }
    public int getCapacity() { return capacity; }

    public void setDriverName(String driverName) { this.driverName = driverName; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public abstract String getType();
}
