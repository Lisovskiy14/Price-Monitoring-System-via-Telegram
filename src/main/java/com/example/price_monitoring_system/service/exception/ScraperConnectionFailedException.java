package com.example.price_monitoring_system.service.exception;

public class ScraperConnectionFailedException extends RuntimeException {
    public ScraperConnectionFailedException(String message) {
        super(message);
    }
}
