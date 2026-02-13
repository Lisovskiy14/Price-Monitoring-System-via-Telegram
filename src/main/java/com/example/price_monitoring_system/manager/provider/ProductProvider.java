package com.example.price_monitoring_system.manager.provider;

import com.example.price_monitoring_system.domain.Product;
import org.jsoup.nodes.Document;

import java.util.Optional;

public interface ProductProvider {
    Optional<Product> provide(String url, Document doc);
}
