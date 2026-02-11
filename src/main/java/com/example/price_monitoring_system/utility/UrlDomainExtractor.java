package com.example.price_monitoring_system.utility;

import java.net.URI;

public class UrlDomainExtractor {

    public static String extractDomain(String url) {
        URI uri = URI.create(url);
        return uri.getHost();
    }
}
