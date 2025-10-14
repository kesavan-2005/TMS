package com.village.service;

import com.village.model.Vehicle;
import com.village.repository.InMemoryRepository;
import java.util.Collection;

public class VehicleServiceImpl implements VehicleService {
    private final InMemoryRepository repo;

    public VehicleServiceImpl(InMemoryRepository repo) { this.repo = repo; }

    @Override
    public void addVehicle(Vehicle v) { repo.saveVehicle(v); }

    @Override
    public Collection<Vehicle> listVehicles() { return repo.findAllVehicles(); }

    @Override
    public void removeVehicle(String id) { repo.deleteVehicle(id); }
}
