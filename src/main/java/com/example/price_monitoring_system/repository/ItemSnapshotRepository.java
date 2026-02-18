package com.example.price_monitoring_system.repository;

import com.example.price_monitoring_system.repository.entity.ItemSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemSnapshotRepository extends JpaRepository<ItemSnapshotEntity, Long> {
//    List<ItemSnapshotEntity> saveAll(List<ItemSnapshotEntity> itemSnapshots);
}
