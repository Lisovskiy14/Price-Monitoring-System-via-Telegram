package com.example.price_monitoring_system.domain;

import com.example.price_monitoring_system.common.Availability;
import com.example.price_monitoring_system.util.ToStringObjectParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Availability availability;

    @Override
    public String toString() {
        return ToStringObjectParser.parse(this);
    }
}
