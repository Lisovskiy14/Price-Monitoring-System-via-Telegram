package com.example.price_monitoring_system.domain;

import com.example.price_monitoring_system.util.ToStringObjectParser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CssSelectorContainer {
    private Long id;
    private String nameSelector;
    private String descriptionSelector;
    private String priceSelector;
    private String availabilitySelector;

    public boolean isEmpty() {
        return nameSelector.isBlank() ||
                priceSelector.isBlank() ||
                availabilitySelector.isBlank();
    }

    @Override
    public String toString() {
        return ToStringObjectParser.parse(this);
    }
}
