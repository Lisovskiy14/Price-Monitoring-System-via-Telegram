package com.example.price_monitoring_system.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemSnapshot {
    private Long id;
    private TrackedItem trackedItem;
    private BigDecimal previousPrice;
    private boolean previousAvailable;
    private LocalDateTime timestamp;
}
