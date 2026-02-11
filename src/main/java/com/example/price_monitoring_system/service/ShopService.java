package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.domain.Shop;

public interface ShopService {
    Shop getShopByDomain(String domain);
    Shop saveShop(String domain);
}
