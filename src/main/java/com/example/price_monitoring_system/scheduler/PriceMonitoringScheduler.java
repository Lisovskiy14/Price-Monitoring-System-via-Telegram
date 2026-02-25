package com.example.price_monitoring_system.scheduler;

import com.example.price_monitoring_system.common.Availability;
import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.dto.ItemSnapshotRequestDto;
import com.example.price_monitoring_system.repository.TrackedItemRepository;
import com.example.price_monitoring_system.repository.mapper.TrackedItemEntityMapper;
import com.example.price_monitoring_system.scraper.ScrapingManager;
import com.example.price_monitoring_system.service.ItemSnapshotService;
import com.example.price_monitoring_system.telegram.TelegramNotifier;
import com.example.price_monitoring_system.util.AvailabilityResolver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceMonitoringScheduler {

    @Value("${paging.page-size:100}")
    private int pageSize;
    @Value("${threads.count:10}")
    private int threadPoolSize;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private Semaphore semaphore;

    @PostConstruct
    public void init() {
        this.semaphore = new Semaphore(threadPoolSize);
    }

    private final ScrapingManager scrapingManager;
    private final TrackedItemRepository trackedItemRepository;
    private final TrackedItemEntityMapper trackedItemEntityMapper;
    private final ItemSnapshotService itemSnapshotService;
    private final TelegramNotifier telegramNotifier;

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void monitorPricesTask() {
        log.info("Starting Monitoring Prices Task...");

        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("id"));
        Page<TrackedItem> page = trackedItemRepository.findAll(pageRequest)
                .map(trackedItemEntityMapper::toTrackedItem);

        while (page.hasContent()) {
            List<ItemSnapshotRequestDto> itemSnapshotRequests = Collections.synchronizedList(new ArrayList<>());

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            page.forEach(trackedItem -> {
                var future = CompletableFuture.runAsync(() -> {
                    try {
                        semaphore.acquire();

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

                    } catch (InterruptedException ex) {
                        log.error("Thread was interrupted with the exception: {}", ex.getMessage());
                        Thread.currentThread().interrupt();
                    } catch (Exception ex) {
                        log.error("Error during Monitoring Prices Task: {}", ex.getMessage());
                    } finally {
                        semaphore.release();
                    }

                }, executorService);

                futures.add(future);
            });

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            if (!itemSnapshotRequests.isEmpty()) {
                processUpdatesAndNotifications(itemSnapshotRequests);
            }

            if (page.hasNext()) {
                page = trackedItemRepository.findAll(page.nextPageable())
                        .map(trackedItemEntityMapper::toTrackedItem);
            } else {
                break;
            }
        }

        log.info("Monitoring Prices Task has been finished.");
    }

    private void processUpdatesAndNotifications(List<ItemSnapshotRequestDto> itemSnapshotRequests) {
        log.info("{} products must be updated.", itemSnapshotRequests.size());
        List<ItemSnapshot> itemSnapshots = itemSnapshotService.saveItemSnapshots(itemSnapshotRequests);

        List<ItemSnapshot> toNotify = itemSnapshots.stream()
                .filter(this::isNotificationRequired)
                .toList();

        if (!toNotify.isEmpty()) {
            log.info("Listeners must be notified for {} updated TrackedItems;", toNotify.size());
            telegramNotifier.notifyAll(toNotify);
        }
    }

    private boolean isNotificationRequired(ItemSnapshot itemSnapshot) {
        Product updatedProduct = itemSnapshot.getTrackedItem().getProduct();
        return becomeAvailable(updatedProduct.getAvailability(), itemSnapshot.getPreviousAvailability()) ||
                isCheaper(updatedProduct, itemSnapshot.getPreviousPrice());
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
}
