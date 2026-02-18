package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.ItemSnapshotRequestDto;
import com.example.price_monitoring_system.repository.ItemSnapshotRepository;
import com.example.price_monitoring_system.repository.ProductRepository;
import com.example.price_monitoring_system.repository.TrackedItemRepository;
import com.example.price_monitoring_system.repository.entity.ItemSnapshotEntity;
import com.example.price_monitoring_system.repository.entity.ProductEntity;
import com.example.price_monitoring_system.repository.entity.TrackedItemEntity;
import com.example.price_monitoring_system.repository.mapper.ItemSnapshotEntityMapper;
import com.example.price_monitoring_system.repository.mapper.ProductEntityMapper;
import com.example.price_monitoring_system.repository.mapper.TrackedItemEntityMapper;
import com.example.price_monitoring_system.service.ItemSnapshotService;
import com.example.price_monitoring_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSnapshotServiceImpl implements ItemSnapshotService {

    private final ItemSnapshotRepository itemSnapshotRepository;
    private final ItemSnapshotEntityMapper itemSnapshotEntityMapper;
    private final ProductRepository productRepository;
    private final TrackedItemRepository trackedItemRepository;

    @Override
    @Transactional
    public List<ItemSnapshot> saveItemSnapshots(List<ItemSnapshotRequestDto> itemSnapshotsRequests) {
        List<Long> productIds = new ArrayList<>();
        List<Long> trackedItemIds = new ArrayList<>();

        for (ItemSnapshotRequestDto request : itemSnapshotsRequests) {
            productIds.add(request.getOldTrackedItem().getProduct().getId());
            trackedItemIds.add(request.getOldTrackedItem().getId());
        }

        Map<Long, TrackedItemEntity> existingTrackedItems = trackedItemRepository.findAllById(trackedItemIds).stream()
                .collect(Collectors.toMap(TrackedItemEntity::getId, trackedItemEntity -> trackedItemEntity));
        Map<Long, ProductEntity> existingProducts = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));

        List<ItemSnapshotEntity> snapshotsToSave = new ArrayList<>();

        for (ItemSnapshotRequestDto request : itemSnapshotsRequests) {
            Product scrapped = request.getNewScrappedProduct();
            TrackedItem oldItem = request.getOldTrackedItem();
            Long productId = oldItem.getProduct().getId();

            ProductEntity productEntity = existingProducts.get(productId);
            if (productEntity != null) {
                productEntity.setName(scrapped.getName());
                productEntity.setDescription(scrapped.getDescription());
                productEntity.setPrice(scrapped.getPrice());
                productEntity.setAvailable(scrapped.isAvailable());
            }

            ItemSnapshotEntity snapshot = ItemSnapshotEntity.builder()
                    .trackedItem(existingTrackedItems.get(oldItem.getId()))
                    .previousPrice(oldItem.getProduct().getPrice())
                    .previousAvailable(oldItem.getProduct().isAvailable())
                    .build();

            snapshotsToSave.add(snapshot);
        }

        log.info("Saving {} new item snapshots.", snapshotsToSave.size());
        return itemSnapshotRepository.saveAllAndFlush(snapshotsToSave).stream()
                .map(itemSnapshotEntityMapper::toItemSnapshot)
                .toList();
    }
}
