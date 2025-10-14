package com.village.service;

import com.village.model.Vehicle;
import java.util.Collection;

public interface VehicleService {
    void addVehicle(Vehicle v);
    Collection<Vehicle> listVehicles();
    void removeVehicle(String id);
}
