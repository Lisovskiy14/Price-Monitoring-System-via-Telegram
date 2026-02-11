package com.example.price_monitoring_system.service.exception;

public class TrackedItemNotFoundException extends RuntimeException {
    private static final String TRACKED_ITEM_WITH_URL_NOT_FOUND = "Tracked item with url %s not found";

    public TrackedItemNotFoundException(String url) {
        super(String.format(TRACKED_ITEM_WITH_URL_NOT_FOUND, url));
    }
}
