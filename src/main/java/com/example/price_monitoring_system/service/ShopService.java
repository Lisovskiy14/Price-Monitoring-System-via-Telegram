package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.domain.Shop;

import java.util.Optional;

public interface ShopService {
    Optional<Shop> getShopByDomain(String domain);
    Shop saveShop(String domain);
}
