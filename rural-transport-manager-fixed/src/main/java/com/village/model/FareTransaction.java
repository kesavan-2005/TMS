package com.village.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record FareTransaction(String id, String bookingId, double amount, LocalDateTime timestamp) implements java.io.Serializable {
    public FareTransaction(String bookingId, double amount) {
        this(UUID.randomUUID().toString(), bookingId, amount, LocalDateTime.now());
    }
}
