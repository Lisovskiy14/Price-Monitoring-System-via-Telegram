package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.TrackedItemRequestDto;
import com.example.price_monitoring_system.service.TrackedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackedItemServiceImpl implements TrackedItemService {

    @Override
    public boolean existsByUrl(String url) {
        return false;
    }

    @Override
    public TrackedItem getTrackedItemByUrl(String url) {
        return null;
    }

    @Override
    public TrackedItem saveTrackedItem(TrackedItemRequestDto trackedItemRequestDto) {
        return null;
    }
}
