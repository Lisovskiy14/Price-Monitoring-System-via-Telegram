package com.example.price_monitoring_system.scheduler;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.dto.ItemSnapshotRequestDto;
import com.example.price_monitoring_system.manager.ScrapingManager;
import com.example.price_monitoring_system.service.ItemSnapshotService;
import com.example.price_monitoring_system.service.TrackedItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceMonitoringScheduler {

    private final ScrapingManager scrapingManager;
    private final TrackedItemService trackedItemService;
    private final ItemSnapshotService itemSnapshotService;

    @Scheduled(fixedRate = 1000 * 60 * 30)
    @Transactional
    public void monitorPricesTask() {
        log.info("Starting monitoring prices task...");

        List<TrackedItem> allItems = trackedItemService.getAllTrackedItems();

        try {
            List<ItemSnapshotRequestDto> itemSnapshotRequests = new ArrayList<>();

            allItems.forEach(trackedItem -> {
                Product scrappedProduct = scrapingManager.scrapProduct(trackedItem.getUrl());

                if (!hasDifferenceBetweenProducts(trackedItem.getProduct(), scrappedProduct)) {
                    return;
                }

                ItemSnapshotRequestDto itemSnapshotRequestDto = ItemSnapshotRequestDto.builder()
                        .oldTrackedItem(trackedItem)
                        .newScrappedProduct(scrappedProduct)
                        .build();

                itemSnapshotRequests.add(itemSnapshotRequestDto);
            });

            if (itemSnapshotRequests.isEmpty()) {
                return;
            }

            List<ItemSnapshot> itemSnapshots = itemSnapshotService.saveItemSnapshots(itemSnapshotRequests);
            List<ItemSnapshot> itemSnapshotsToNotify = new ArrayList<>();

            itemSnapshots.forEach(itemSnapshot -> {
                Product updatedProduct = itemSnapshot.getTrackedItem().getProduct();
                if (becomeAvailable(updatedProduct, itemSnapshot.isPreviousAvailable()) ||
                        isCheaper(updatedProduct, itemSnapshot.getPreviousPrice())) {
                    itemSnapshotsToNotify.add(itemSnapshot);
                }
            });

            if (!itemSnapshotsToNotify.isEmpty()) {
                return;
            }

            // Need to call NotificationService here.

        } catch (RuntimeException ex) {
            log.error("Error during monitoring prices task: {}", ex.getMessage());
        }
    }

    private boolean hasDifferenceBetweenProducts(Product oldProduct, Product newProduct) {
        boolean newName = !oldProduct.getName().equals(newProduct.getName());
        boolean newDescription = !oldProduct.getDescription().equals(newProduct.getDescription());
        boolean newPrice = !oldProduct.getPrice().equals(newProduct.getPrice());
        boolean newAvailable = !(oldProduct.isAvailable() == newProduct.isAvailable());
        return (newName || newDescription || newPrice || newAvailable);
    }

    private boolean becomeAvailable(Product updatedProduct, boolean isPreviousAvailable) {
        if (isPreviousAvailable) {
            return false;
        }
        return updatedProduct.isAvailable();
    }

    private boolean isCheaper(Product updatedProduct, BigDecimal previousPrice) {
        return updatedProduct.getPrice().compareTo(previousPrice) < 0;
    }
}
