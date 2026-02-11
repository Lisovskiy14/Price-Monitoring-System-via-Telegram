package com.example.price_monitoring_system.service.exception;

public class ScrapingProductFailedException extends RuntimeException {
    private static final String SCRAPING_PRODUCT_FAILED_WITH_URL = "Scraping product failed with url %s";

    public ScrapingProductFailedException(String url) {
        super(String.format(SCRAPING_PRODUCT_FAILED_WITH_URL, url));
    }
}
