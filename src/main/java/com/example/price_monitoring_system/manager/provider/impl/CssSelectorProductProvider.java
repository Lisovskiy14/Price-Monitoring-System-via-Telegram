package com.example.price_monitoring_system.manager.provider.impl;

import com.example.price_monitoring_system.domain.CssSelectorContainer;
import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.repository.ShopRepository;
import com.example.price_monitoring_system.repository.entity.ShopEntity;
import com.example.price_monitoring_system.utility.AvailabilityResolver;
import com.example.price_monitoring_system.utility.HtmlDocumentProvider;
import com.example.price_monitoring_system.manager.provider.ProductProvider;
import com.example.price_monitoring_system.repository.entity.CssSelectorContainerEntity;
import com.example.price_monitoring_system.repository.mapper.CssSelectorContainerEntityMapper;
import com.example.price_monitoring_system.utility.UrlDomainExtractor;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Order(20)
@Component
@RequiredArgsConstructor
public class CssSelectorProductProvider implements ProductProvider {

    private final ShopRepository shopRepository;
    private final CssSelectorContainerEntityMapper containerEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> provide(String url, Document doc) {

        String domain = UrlDomainExtractor.extractDomain(url);

        ShopEntity shopEntity = shopRepository.findByDomain(domain)
                .orElse(null);

        if (shopEntity == null) {
            return Optional.empty();
        }

        CssSelectorContainerEntity selectorContainerEntity = shopEntity.getCssSelectorContainer();

        if (selectorContainerEntity == null) {
            return Optional.empty();
        }

        CssSelectorContainer selectorContainer = containerEntityMapper
                .toCssSelectorContainer(selectorContainerEntity);

        return Optional.of(provideProductBySelectorContainer(doc, selectorContainer));
    }

    private Product provideProductBySelectorContainer(Document doc, CssSelectorContainer cssSelectorContainer) {
        Product product = new Product();

        Element field = doc.selectFirst(cssSelectorContainer.getNameSelector());
        if (field != null) {
            product.setName(field.data());
        }

        field = doc.selectFirst(cssSelectorContainer.getDescriptionSelector());
        if (field != null) {
            product.setDescription(field.data());
        }

        field = doc.selectFirst(cssSelectorContainer.getPriceSelector());
        if (field != null) {
            product.setPrice(BigDecimal.valueOf(Double.parseDouble(field.data())));
        }

        field = doc.selectFirst(cssSelectorContainer.getAvailabilitySelector());
        if (field != null) {
            String url = field.data();
            product.setAvailable(AvailabilityResolver.resolveByUrl(url));
        }

        return product;
    }
}
