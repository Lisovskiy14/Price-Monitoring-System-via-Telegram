package com.example.price_monitoring_system.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrackedItem {
    private Long id;
    private Product product;
    private Shop shop;
    private String url;
    private LocalDateTime lastChecked;
    private List<Long> listenerIds;

    @Override
    public String toString() {
        return String.format("[%s : %s]", shop.getDomain(), product.getName());
    }
}
