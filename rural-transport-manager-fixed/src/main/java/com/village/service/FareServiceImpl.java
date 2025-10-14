package com.village.service;

import com.village.model.FareTransaction;
import com.village.repository.InMemoryRepository;

import java.util.List;

public class FareServiceImpl implements FareService {
    private final InMemoryRepository repo;

    public FareServiceImpl(InMemoryRepository repo) { this.repo = repo; }

    @Override
    public FareTransaction recordFare(String bookingId, double amount) {
        FareTransaction t = new FareTransaction(bookingId, amount);
        repo.addTransaction(t);
        return t;
    }

    @Override
    public List<FareTransaction> listTransactions() { return repo.findAllTransactions(); }

    @Override
    public double calculateFare(double distanceKm, String vehicleType) {
        double base = switch (vehicleType == null ? "AUTO" : vehicleType.toUpperCase()) {
            case "BUS" -> 5.0;
            case "AUTO" -> 3.0;
            default -> 4.0;
        };
        return base * distanceKm;
    }
}
