package com.example.price_monitoring_system.util;

import java.net.URI;

public class UrlDomainExtractor {

    public static String extractDomain(String url) {
        URI uri = URI.create(url);
        return uri.getHost();
    }
}
