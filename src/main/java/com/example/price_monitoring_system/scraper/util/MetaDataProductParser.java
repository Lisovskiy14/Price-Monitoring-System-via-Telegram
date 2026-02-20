package com.example.price_monitoring_system.scraper.util;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.util.AvailabilityResolver;
import com.fasterxml.jackson.databind.JsonNode;

public class MetaDataProductParser {

    public static Product parse(JsonNode root) {
        return Product.builder()
                .name(root.path("name").asText())
                .description(root.path("description").asText())
                .price(root.path("offers").path("price").decimalValue())
                .availability(AvailabilityResolver.resolveByUrl(
                        root.path("offers").path("availability").asText()))
                .build();
    }
}
