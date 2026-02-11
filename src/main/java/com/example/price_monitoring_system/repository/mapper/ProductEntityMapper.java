package com.example.price_monitoring_system.repository.mapper;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.repository.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    Product toProduct(ProductEntity productEntity);
    ProductEntity toProductEntity(Product product);
}
