package com.example.price_monitoring_system.repository;

import com.example.price_monitoring_system.repository.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
}
