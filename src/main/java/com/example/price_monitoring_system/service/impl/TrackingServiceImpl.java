package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.Shop;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.domain.User;
import com.example.price_monitoring_system.dto.RegistrationResult;
import com.example.price_monitoring_system.manager.ScrapingManager;
import com.example.price_monitoring_system.service.ShopService;
import com.example.price_monitoring_system.service.TrackedItemService;
import com.example.price_monitoring_system.service.TrackingService;
import com.example.price_monitoring_system.service.UserService;
import com.example.price_monitoring_system.service.exception.ScrapingProductFailedException;
import com.example.price_monitoring_system.utility.UrlDomainExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final TrackedItemService trackedItemService;
    private final ScrapingManager scrapingManager;
    private final ShopService shopService;
    private final UserService userService;

    @Override
    @Transactional
    public RegistrationResult registerTrackedItem(String url, Long listenerId) {

        if (trackedItemService.existsByUrl(url)) {
            User newListener = User.builder()
                    .id(listenerId)
                    .build();
            return updateTrackedItemListeners(url, newListener);
        }

        String domain = UrlDomainExtractor.extractDomain(url);
        Shop shop = shopService.getShopByDomain(domain)
                .orElseGet(() -> shopService.saveShop(domain));

        Product product = scrapingManager.scrapProduct(url)
                .orElseThrow(() -> new ScrapingProductFailedException(url));

        User listener = User.builder()
                .id(listenerId)
                .build();

        listener = userService.saveUser(listener);

        TrackedItem trackedItem = TrackedItem.builder()
                .url(url)
                .shop(shop)
                .product(product)
                .listeners(new java.util.ArrayList<>())
                .build();
        trackedItem.addListener(listener);

        trackedItem = trackedItemService.saveTrackedItem(trackedItem);
        log.info("New TrackedItem was registered: {}", trackedItem.getUrl());

        return RegistrationResult.builder()
                .trackedItem(trackedItem)
                .alreadyTracked(false)
                .build();
    }

    private RegistrationResult updateTrackedItemListeners(String url, User newListener) {
        TrackedItem trackedItem = trackedItemService.getTrackedItemByUrl(url);

        if (trackedItem.getListeners().contains(newListener)) {
            return RegistrationResult.builder()
                    .trackedItem(trackedItem)
                    .alreadyTracked(true)
                    .build();
        }

        trackedItem.addListener(newListener);
        trackedItem = trackedItemService.updateTrackedItem(trackedItem);
        return RegistrationResult.builder()
                .trackedItem(trackedItem)
                .alreadyTracked(false)
                .build();
    }
}
