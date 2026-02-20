package com.example.price_monitoring_system.scheduler;

import com.example.price_monitoring_system.common.Availability;
import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.dto.ItemSnapshotRequestDto;
import com.example.price_monitoring_system.scraper.ScrapingManager;
import com.example.price_monitoring_system.service.ItemSnapshotService;
import com.example.price_monitoring_system.service.TrackedItemService;
import com.example.price_monitoring_system.telegram.TelegramNotifier;
import com.example.price_monitoring_system.util.AvailabilityResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceMonitoringScheduler {

    private final ScrapingManager scrapingManager;
    private final TrackedItemService trackedItemService;
    private final ItemSnapshotService itemSnapshotService;
    private final TelegramNotifier telegramNotifier;

    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Transactional
    public void monitorPricesTask() {
        log.info("Starting Monitoring Prices Task...");

        List<TrackedItem> allItems = trackedItemService.getAllTrackedItems();

        try {
            List<ItemSnapshotRequestDto> itemSnapshotRequests = new ArrayList<>();

            allItems.forEach(trackedItem -> {

                Optional<Product> scrappedProductOpt = scrapingManager.scrapProduct(trackedItem.getUrl());
                if (scrappedProductOpt.isEmpty()) {
                    return;
                }

                Product scrappedProduct = scrappedProductOpt.get();

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
                finishTask();
                return;
            }

            log.info("{} products must be updated.", itemSnapshotRequests.size());
            List<ItemSnapshot> itemSnapshots = itemSnapshotService.saveItemSnapshots(itemSnapshotRequests);
            List<ItemSnapshot> itemSnapshotsToNotify = new ArrayList<>();

            itemSnapshots.forEach(itemSnapshot -> {
                Product updatedProduct = itemSnapshot.getTrackedItem().getProduct();
                if (becomeAvailable(updatedProduct.getAvailability(), itemSnapshot.getPreviousAvailability()) ||
                        isCheaper(updatedProduct, itemSnapshot.getPreviousPrice())) {
                    itemSnapshotsToNotify.add(itemSnapshot);
                }
            });

            if (itemSnapshotsToNotify.isEmpty()) {
                finishTask();
                return;
            }

            log.info("Listeners must be notified for {} updated TrackedItems;", itemSnapshotsToNotify.size());
            telegramNotifier.notifyAll(itemSnapshotsToNotify);
            finishTask();

        } catch (RuntimeException ex) {
            log.error("Error during Monitoring Prices Task: {}", ex.getMessage());
        }
    }

    private boolean hasDifferenceBetweenProducts(Product oldProduct, Product newProduct) {
        boolean newName = !oldProduct.getName().equals(newProduct.getName());
        boolean newDescription = !oldProduct.getDescription().equals(newProduct.getDescription());
        boolean newPrice = oldProduct.getPrice().compareTo(newProduct.getPrice()) != 0;
        boolean newAvailable = !(oldProduct.getAvailability().equals(newProduct.getAvailability()));
        return (newName || newDescription || newPrice || newAvailable);
    }

    private boolean becomeAvailable(Availability newAvailability, Availability prevAvailability) {
        return AvailabilityResolver.becomeAvailable(newAvailability, prevAvailability);
    }

    private boolean isCheaper(Product updatedProduct, BigDecimal previousPrice) {
        return updatedProduct.getPrice().compareTo(previousPrice) < 0;
    }

    private void finishTask() {
        log.info("Monitoring Prices Task has been finished.");
    }
}
