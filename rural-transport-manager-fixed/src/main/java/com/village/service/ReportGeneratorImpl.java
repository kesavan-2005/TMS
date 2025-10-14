package com.village.service;

import com.village.repository.InMemoryRepository;

import java.util.stream.Collectors;

public class ReportGeneratorImpl implements ReportGenerator {
    private final InMemoryRepository repo;

    public ReportGeneratorImpl(InMemoryRepository repo) { this.repo = repo; }

    @Override
    public String generateSummaryReport() {
        var vehicles = repo.findAllVehicles();
        var bookings = repo.findAllBookings();
        var transactions = repo.findAllTransactions();

        StringBuilder sb = new StringBuilder();
        sb.append("=== SUMMARY REPORT ===\n");
        sb.append("Vehicles: ").append(vehicles.size()).append("\n");
        sb.append("Bookings: ").append(bookings.size()).append("\n");
        sb.append("Transactions: ").append(transactions.size()).append("\n\nTransactions detail:\n");
        sb.append(transactions.stream().map(t -> t.id() + ":" + t.amount()).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
