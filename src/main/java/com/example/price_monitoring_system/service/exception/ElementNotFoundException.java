package com.example.price_monitoring_system.service.exception;

public class ElementNotFoundException extends RuntimeException {
    private static final String ELEMENT_BY_CSS_SELECTOR_NOT_FOUND = "Element by css selector %S not found";

    public ElementNotFoundException(String cssSelector) {
        super(String.format(ELEMENT_BY_CSS_SELECTOR_NOT_FOUND, cssSelector));
    }
}
