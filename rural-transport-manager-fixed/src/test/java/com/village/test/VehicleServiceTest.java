package com.village.test;

import com.village.repository.InMemoryRepository;
import com.village.service.VehicleServiceImpl;
import com.village.model.Vehicle;
import com.village.model.VehicleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VehicleServiceTest {
    @Test
    public void testAddAndList() {
        InMemoryRepository repo = new InMemoryRepository();
        VehicleServiceImpl service = new VehicleServiceImpl(repo);
        Vehicle v = new Vehicle(java.util.UUID.randomUUID().toString(), "D", "TN-01-AB-1234", 4, VehicleType.AUTO);
        service.addVehicle(v);
        Assertions.assertTrue(service.listVehicles().stream().anyMatch(x -> x.getId().equals(v.getId())));
    }
}
