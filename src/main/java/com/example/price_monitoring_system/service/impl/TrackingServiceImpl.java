package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.Shop;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.domain.User;
import com.example.price_monitoring_system.dto.TrackedItemRequestDto;
import com.example.price_monitoring_system.manager.ScrapingManager;
import com.example.price_monitoring_system.manager.exception.ProductNotFoundException;
import com.example.price_monitoring_system.service.ShopService;
import com.example.price_monitoring_system.service.TrackedItemService;
import com.example.price_monitoring_system.service.TrackingService;
import com.example.price_monitoring_system.service.UserService;
import com.example.price_monitoring_system.service.exception.ScrapingProductFailedException;
import com.example.price_monitoring_system.service.exception.ShopNotFoundException;
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
    public TrackedItem registerTrackedItem(TrackedItemRequestDto trackedItemRequestDto) {
        String url = trackedItemRequestDto.getUrl();
        if (trackedItemService.existsByUrl(url)) {
            User newListener = User.builder()
                    .id(trackedItemRequestDto.getListenerId())
                    .build();
            return updateTrackedItemListeners(url, newListener);
        }

        String domain = UrlDomainExtractor.extractDomain(url);
        Shop shop = shopService.getShopByDomain(domain)
                .orElseGet(() -> shopService.saveShop(domain));

        Product product;
        try {
            product = scrapingManager.scrapProduct(url);
        } catch (ProductNotFoundException ex) {
            throw new ScrapingProductFailedException(url);
        }

        User listener = User.builder()
                .id(trackedItemRequestDto.getListenerId())
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
        log.info("New TrackedItem was registered: {}", trackedItem);

        return trackedItem;
    }

    @Override
    public void checkTrackedItems() {

    }

    private TrackedItem updateTrackedItemListeners(String url, User newListener) {
        TrackedItem trackedItem = trackedItemService.getTrackedItemByUrl(url);
        trackedItem.addListener(newListener);
        return trackedItemService.updateTrackedItem(trackedItem);
    }
}
