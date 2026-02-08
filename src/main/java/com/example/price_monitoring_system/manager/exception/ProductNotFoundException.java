package com.example.price_monitoring_system.manager.exception;

public class ProductNotFoundException extends RuntimeException {
    private static final String CSS_SELECTOR_NOT_FOUND = "Css selector not found for url %s";

    public ProductNotFoundException(String url) {
        super(String.format(CSS_SELECTOR_NOT_FOUND, url));
    }
}
