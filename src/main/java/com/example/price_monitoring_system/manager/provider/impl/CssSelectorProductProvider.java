package com.example.price_monitoring_system.manager.provider.impl;

import com.example.price_monitoring_system.domain.CssSelectorContainer;
import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.utility.HtmlDocumentProvider;
import com.example.price_monitoring_system.manager.provider.ProductProvider;
import com.example.price_monitoring_system.repository.CssSelectorContainerRepository;
import com.example.price_monitoring_system.repository.entity.CssSelectorContainerEntity;
import com.example.price_monitoring_system.repository.mapper.CssSelectorContainerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@Order(20)
@Component
@RequiredArgsConstructor
public class CssSelectorProductProvider implements ProductProvider {

    private final CssSelectorContainerRepository containerRepository;
    private final CssSelectorContainerEntityMapper containerEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> provide(String url) {
        Document doc = HtmlDocumentProvider.provideHtmlDoc(url);

        String domain = extractDomain(url);

        CssSelectorContainerEntity selectorContainerEntity = containerRepository.findByShop_Domain(domain)
                .orElse(null);

        if (selectorContainerEntity == null) {
            return Optional.empty();
        }

        CssSelectorContainer selectorContainer = containerEntityMapper
                .toCssSelectorContainer(selectorContainerEntity);

        return Optional.of(provideProductBySelectorContainer(doc, selectorContainer));
    }

    private String extractDomain(String url) {
        URI uri = URI.create(url);
        return uri.getHost();
    }

    private Product provideProductBySelectorContainer(Document doc, CssSelectorContainer cssSelectorContainer) {
        Product product = new Product();

        Element field = doc.selectFirst(cssSelectorContainer.getName());
        if (field != null) {
            product.setName(field.data());
        }

        field = doc.selectFirst(cssSelectorContainer.getDescription());
        if (field != null) {
            product.setDescription(field.data());
        }

        field = doc.selectFirst(cssSelectorContainer.getPrice());
        if (field != null) {
            product.setPrice(BigDecimal.valueOf(Double.parseDouble(field.data())));
        }

        field = doc.selectFirst(cssSelectorContainer.getAvailability());
        if (field != null) {
            String url = field.data();
            String availabilityType = url.substring(url.lastIndexOf("/") + 1)
                    .replaceAll("[^a-zA-Z]", "")
                    .toLowerCase();
            if (availabilityType.equals("instock")) {
                product.setAvailable(true);
            } else {
                product.setAvailable(false);
            }
        }

        return product;
    }
}
