package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.TrackedItemRequestDto;

public interface TrackedItemService {
    boolean existsByUrl(String url);
    TrackedItem getTrackedItemByUrl(String url);
    TrackedItem saveTrackedItem(TrackedItemRequestDto trackedItemRequestDto);
}
