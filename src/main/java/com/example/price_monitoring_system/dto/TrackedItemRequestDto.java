package com.example.price_monitoring_system.dto;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.Shop;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TrackedItemRequestDto {
    String url;
    Product product;
    Shop shop;
    Long listenerId;
}
