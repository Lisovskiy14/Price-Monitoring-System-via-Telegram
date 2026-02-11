package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.manager.ScrapingManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ScrapingManager scrapingManager;

     public Product getProductByUrl(String url) {
         return scrapingManager.scrapProduct(url);
     }
}
