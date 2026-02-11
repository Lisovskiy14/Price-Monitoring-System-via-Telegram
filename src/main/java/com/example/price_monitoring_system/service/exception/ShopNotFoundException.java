package com.example.price_monitoring_system.service.exception;

public class ShopNotFoundException extends RuntimeException {
    private static final String SHOP_WITH_DOMAIN_NOT_FOUND = "Shop with domain %s not found";

    public ShopNotFoundException(String domain) {
        super(String.format(SHOP_WITH_DOMAIN_NOT_FOUND, domain));
    }
}
