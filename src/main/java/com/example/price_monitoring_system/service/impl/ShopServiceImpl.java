package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.Shop;
import com.example.price_monitoring_system.repository.ShopRepository;
import com.example.price_monitoring_system.repository.entity.ShopEntity;
import com.example.price_monitoring_system.repository.mapper.ShopEntityMapper;
import com.example.price_monitoring_system.service.ShopService;
import com.example.price_monitoring_system.service.exception.ShopNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final ShopEntityMapper shopEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Shop> getShopByDomain(String domain) {
        return shopRepository.findByDomain(domain)
                .map(shopEntityMapper::toShop);
    }

    @Override
    @Transactional
    public Shop saveShop(String domain) {
        Shop shop = Shop.builder()
                .domain(domain)
                .build();

        ShopEntity shopEntity = shopRepository.saveAndFlush(
                shopEntityMapper.toShopEntity(shop));

        return shopEntityMapper.toShop(shopEntity);
    }
}
