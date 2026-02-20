package com.example.price_monitoring_system.common;

public enum Availability {
    BACK_ORDER("back-order"),
    DISCONTINUED("discontinued"),
    IN_STOCK("in-stock"),
    IN_STORE_ONLY("in-store-only"),
    LIMITED_AVAILABILITY("limited-availability"),
    MADE_TO_ORDER("made-to-order"),
    ONLINE_ONLY("online-only"),
    OUT_OF_STOCK("out-of-stock"),
    PRE_ORDER("pre-order"),
    PRE_SALE("pre-sale"),
    RESERVED("reserved"),
    SOLD_OUT("sold-out");

    private final String value;

    Availability(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
