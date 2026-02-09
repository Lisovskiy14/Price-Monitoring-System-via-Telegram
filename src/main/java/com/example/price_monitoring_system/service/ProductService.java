package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.manager.ProductManager;
import com.example.price_monitoring_system.manager.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductManager productManager;

     public Product getProductByUrl(String url) {

         try {
             return productManager.getProduct(url);
         } catch (ProductNotFoundException ex) {

         }

     }
}
