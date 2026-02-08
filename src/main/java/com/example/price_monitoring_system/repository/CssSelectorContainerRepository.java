package com.example.price_monitoring_system.repository;

import com.example.price_monitoring_system.repository.entity.CssSelectorContainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CssSelectorContainerRepository extends JpaRepository<CssSelectorContainerEntity, Long> {
    Optional<CssSelectorContainerEntity> findByShop_Domain(String shopDomain);
}
