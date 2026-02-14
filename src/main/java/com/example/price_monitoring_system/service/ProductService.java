package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> updateProducts(List<Product> products);
}
