package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.TrackedItemRequestDto;

import java.util.List;

public interface TrackedItemService {
    boolean existsByUrl(String url);
    List<TrackedItem> getAllTrackedItems();
    List<TrackedItem> getTrackedItemsByListenerId(Long listenerId);
    TrackedItem getTrackedItemByUrl(String url);
    TrackedItem saveTrackedItem(TrackedItem trackedItem);
    TrackedItem updateTrackedItem(TrackedItem trackedItem);
    void removeListenerFromTrackedItems(List<String> urls, Long listenerId);
}
