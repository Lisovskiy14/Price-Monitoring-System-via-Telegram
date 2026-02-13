package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.domain.User;
import com.example.price_monitoring_system.dto.TrackedItemRequestDto;
import com.example.price_monitoring_system.repository.TrackedItemRepository;
import com.example.price_monitoring_system.repository.entity.TrackedItemEntity;
import com.example.price_monitoring_system.repository.mapper.TrackedItemEntityMapper;
import com.example.price_monitoring_system.service.TrackedItemService;
import com.example.price_monitoring_system.service.exception.TrackedItemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TrackedItemServiceImpl implements TrackedItemService {

    private final TrackedItemRepository trackedItemRepository;
    private final TrackedItemEntityMapper trackedItemEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUrl(String url) {
        return trackedItemRepository.existsByUrl(url);
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedItem getTrackedItemByUrl(String url) {
        TrackedItemEntity trackedItemEntity = trackedItemRepository.findByUrl(url)
                .orElseThrow(() -> new TrackedItemNotFoundException(url));
        return trackedItemEntityMapper.toTrackedItem(trackedItemEntity);
    }

    @Override
    @Transactional
    public TrackedItem saveTrackedItem(TrackedItem trackedItem) {
        TrackedItemEntity trackedItemEntity = trackedItemRepository.saveAndFlush(
                trackedItemEntityMapper.toTrackedItemEntity(trackedItem));
        return trackedItemEntityMapper.toTrackedItem(trackedItemEntity);
    }

    @Override
    @Transactional
    public TrackedItem updateTrackedItem(TrackedItem trackedItem) {
        TrackedItemEntity trackedItemEntity = trackedItemRepository.saveAndFlush(
                trackedItemEntityMapper.toTrackedItemEntity(trackedItem));
        return trackedItemEntityMapper.toTrackedItem(trackedItemEntity);
    }
}
