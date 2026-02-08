package com.example.price_monitoring_system.manager;

import com.example.price_monitoring_system.domain.CssSelectorContainer;
import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.manager.exception.ProductNotFoundException;
import com.example.price_monitoring_system.manager.provider.ProductProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductManager {

    private final List<ProductProvider> providers;

    public Product getProduct(String url) {
        for (ProductProvider provider : providers) {
            return provider.provide(url)
                    .orElseThrow(() -> new ProductNotFoundException(url));
        }

        throw new ProductNotFoundException(url);
    }
}
