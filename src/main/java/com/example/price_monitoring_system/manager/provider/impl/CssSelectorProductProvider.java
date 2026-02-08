package com.example.price_monitoring_system.manager.provider.impl;

import com.example.price_monitoring_system.domain.CssSelectorContainer;
import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.manager.HtmlDocumentProvider;
import com.example.price_monitoring_system.manager.provider.ProductProvider;
import com.example.price_monitoring_system.repository.CssSelectorContainerRepository;
import com.example.price_monitoring_system.repository.entity.CssSelectorContainerEntity;
import com.example.price_monitoring_system.repository.mapper.CssSelectorContainerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        CssSelectorContainer selectorContainer;
        if (selectorContainerEntity != null) {
            selectorContainer = containerEntityMapper
                    .toCssSelectorContainer(selectorContainerEntity);
        } else {
            selectorContainer = getCssSelectors(doc);
            if (selectorContainer == null) {
                return Optional.empty();
            }
        }

        return Optional.of(provideProductBySelectorContainer(doc, selectorContainer));
    }

    private String extractDomain(String url) {
        URI uri = URI.create(url);
        return uri.getHost();
    }

    private CssSelectorContainer getCssSelectors(Document doc) {

    }

    private Product provideProductBySelectorContainer(Document doc, CssSelectorContainer cssSelectorContainer) {
        return Product.builder().build();
    }
}
