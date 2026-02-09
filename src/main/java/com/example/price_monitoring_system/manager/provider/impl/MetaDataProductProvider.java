package com.example.price_monitoring_system.manager.provider.impl;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.utility.HtmlDocumentProvider;
import com.example.price_monitoring_system.manager.provider.ProductProvider;
import com.example.price_monitoring_system.utility.ProductParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Order(10)
@Component
public class MetaDataProductProvider implements ProductProvider {
    private final String selector = "script[type=application/ld+json]";

    @Override
    public Optional<Product> provide(String url) {
        Document doc = HtmlDocumentProvider.provideHtmlDoc(url);

        Elements scripts = doc.select(selector);
        ObjectMapper mapper = new ObjectMapper();

        for (Element script : scripts) {
            try {

                JsonNode root = mapper.readTree(script.data());

                if ("Product".equals(root.get("@type").asText())) {
                    return Optional.of(ProductParser.parse(root));
                }

            } catch (JsonProcessingException ex) {
                log.info("JSON-LD Parsing Exception: {}", ex.getMessage());
            }
        }

        return Optional.empty();
    }
}
