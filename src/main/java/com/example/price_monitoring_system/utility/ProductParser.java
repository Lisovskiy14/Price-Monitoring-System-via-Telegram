package com.example.price_monitoring_system.utility;

import com.example.price_monitoring_system.domain.Product;
import com.fasterxml.jackson.databind.JsonNode;

public class ProductParser {

    public static Product parse(JsonNode root) {
        return Product.builder()
                .name(root.path("name").asText())
                .description(root.path("description").asText())
                .price(root.path("offer").path("price").decimalValue())
                .available(root.path("offer").path("availability").asBoolean())
                .build();
    }
}
