package com.example.price_monitoring_system.repository;

import com.example.price_monitoring_system.repository.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
    Optional<ShopEntity> findByDomain(String domain);
}
