package com.example.price_monitoring_system.repository.mapper;

import com.example.price_monitoring_system.domain.Shop;
import com.example.price_monitoring_system.repository.entity.ShopEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CssSelectorContainerEntityMapper.class)
public interface ShopEntityMapper {
    Shop toShop(ShopEntity shopEntity);
    ShopEntity toShopEntity(Shop shop);
}
