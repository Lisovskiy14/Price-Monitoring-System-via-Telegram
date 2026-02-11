package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.Shop;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.TrackedItemRequestDto;
import com.example.price_monitoring_system.manager.ScrapingManager;
import com.example.price_monitoring_system.manager.exception.ProductNotFoundException;
import com.example.price_monitoring_system.service.ShopService;
import com.example.price_monitoring_system.service.TrackedItemService;
import com.example.price_monitoring_system.service.TrackingService;
import com.example.price_monitoring_system.service.exception.ShopNotFoundException;
import com.example.price_monitoring_system.utility.UrlDomainExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final TrackedItemService trackedItemService;
    private final ScrapingManager scrapingManager;
    private final ShopService shopService;

    @Override
    @Transactional
    public TrackedItem registerTrackedItem(TrackedItemRequestDto trackedItemRequestDto) {
        String url = trackedItemRequestDto.getUrl();
        if (trackedItemService.existsByUrl(url)) {
            return trackedItemService.getTrackedItemByUrl(url);
        }

        String domain = UrlDomainExtractor.extractDomain(url);
        Shop shop;
        try {
            shop = shopService.getShopByDomain(domain);
        } catch (ShopNotFoundException ex) {
            shop = shopService.saveShop(domain);
        }

        Product product;
        try {
            product = scrapingManager.scrapProduct(url);
        } catch (ProductNotFoundException ex) {
            handleProductNotFound(ex);
            return null;
        }

        TrackedItemRequestDto newTrackedItemRequestDto = TrackedItemRequestDto.builder()
                .url(url)
                .shop(shop)
                .product(product)
                .listenerId(trackedItemRequestDto.getListenerId())
                .build();

        TrackedItem trackedItem = trackedItemService.saveTrackedItem(newTrackedItemRequestDto);
        log.info("New TrackedItem was registered: {}", trackedItem);

        return trackedItem;
    }

    private void handleProductNotFound(ProductNotFoundException ex) {
        return;
    }
}
