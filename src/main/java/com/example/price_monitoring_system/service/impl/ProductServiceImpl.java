package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.repository.ProductRepository;
import com.example.price_monitoring_system.repository.entity.ProductEntity;
import com.example.price_monitoring_system.repository.mapper.ProductEntityMapper;
import com.example.price_monitoring_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    @Transactional
    public List<Product> updateProducts(List<Product> products) {
        Map<Long, Product> scrappedProductMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<ProductEntity> existingProductEntities = productRepository.findAllById(scrappedProductMap.keySet());

        existingProductEntities.forEach(productEntity -> {
            Product scrappedProduct = scrappedProductMap.get(productEntity.getId());
            if (scrappedProduct != null) {
                productEntity.setName(scrappedProduct.getName());
                productEntity.setDescription(scrappedProduct.getDescription());
                productEntity.setPrice(scrappedProduct.getPrice());
                productEntity.setAvailability(scrappedProduct.getAvailability());
                log.info("Product '{}' was updated", productEntity.getId());
            }
        });

        return existingProductEntities.stream()
                .map(productEntityMapper::toProduct)
                .toList();
    }
}
