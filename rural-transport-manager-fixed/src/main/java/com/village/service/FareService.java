package com.village.service;

import com.village.model.FareTransaction;

import java.util.List;

public interface FareService {
    FareTransaction recordFare(String bookingId, double amount);
    List<FareTransaction> listTransactions();
    double calculateFare(double distanceKm, String vehicleType);
}
