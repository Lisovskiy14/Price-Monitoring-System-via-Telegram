package com.example.price_monitoring_system.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CssSelectorContainer {
    private Long id;
    private String name;
    private String description;
    private String price;
    private String availability;

    public boolean isEmpty() {
        return name.isBlank() ||
                price.isBlank() ||
                availability.isBlank();
    }
}
