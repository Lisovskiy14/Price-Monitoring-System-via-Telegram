package com.example.price_monitoring_system.manager;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.manager.exception.ProductNotFoundException;
import com.example.price_monitoring_system.manager.provider.ProductProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScrapingManager {

    private final List<ProductProvider> providers;

    public Product scrapProduct(String url) {
        for (ProductProvider provider : providers) {
            Optional<Product> productOpt = provider.provide(url);
            if (productOpt.isPresent()) {
                return productOpt.get();
            }
        }

        throw new ProductNotFoundException(url);
    }
}
