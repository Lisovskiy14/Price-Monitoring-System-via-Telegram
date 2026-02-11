package com.example.price_monitoring_system.repository;

import com.example.price_monitoring_system.repository.entity.TrackedItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackedItemRepository extends JpaRepository<TrackedItemEntity, Long> {
    boolean existsByUrl(String url);
    Optional<TrackedItemEntity> findByUrl(String url);
}
