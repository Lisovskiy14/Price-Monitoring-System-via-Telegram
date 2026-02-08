package com.example.price_monitoring_system.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrackedItem {
    private Long id;
    private Product product;
    private Shop shop;
    private String url;
    private LocalDateTime lastChecked;
}
