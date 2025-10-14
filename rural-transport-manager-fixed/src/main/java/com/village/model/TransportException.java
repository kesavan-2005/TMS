package com.village.model;

public class TransportException extends RuntimeException {
    public TransportException(String message) { super(message); }
    public TransportException(String message, Throwable cause) { super(message, cause); }
}
