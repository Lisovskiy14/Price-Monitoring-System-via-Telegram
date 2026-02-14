package com.example.price_monitoring_system.dto;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.TrackedItem;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemSnapshotRequestDto {
    TrackedItem trackedItem;
    Product scrapedProduct;
}
