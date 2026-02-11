package com.example.price_monitoring_system.repository.mapper;

import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.repository.entity.TrackedItemEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductEntityMapper.class,
                ShopEntityMapper.class,
                UserEntityMapper.class
        }
)
public interface TrackedItemEntityMapper {
    TrackedItem toTrackedItem(TrackedItemEntity trackedItemEntity);
    TrackedItemEntity toTrackedItemEntity(TrackedItem trackedItem);
}
