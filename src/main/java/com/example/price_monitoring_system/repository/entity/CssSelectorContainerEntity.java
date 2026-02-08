package com.example.price_monitoring_system.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "css_selector_containers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CssSelectorContainerEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;

    @Column(name = "name_selector")
    private String nameSelector;

    @Column(name = "description_selector")
    private String descriptionSelector;

    @Column(name = "price_selector")
    private String priceSelector;

    @Column(name = "availability_selector")
    private String availabilitySelector;
}
