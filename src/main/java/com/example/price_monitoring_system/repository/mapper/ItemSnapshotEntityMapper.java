package com.example.price_monitoring_system.repository.mapper;

import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.repository.entity.ItemSnapshotEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TrackedItemEntityMapper.class)
public interface ItemSnapshotEntityMapper {
    ItemSnapshot toItemSnapshot(ItemSnapshotEntity itemSnapshotEntity);
}
