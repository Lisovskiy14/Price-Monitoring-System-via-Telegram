package com.example.price_monitoring_system.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shops")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_seq_generator")
    @SequenceGenerator(
            name = "shop_seq_generator",
            sequenceName = "shop_seq"
    )
    private Long id;

    @Column(name = "domain", nullable = false, unique = true)
    private String domain;

    @OneToOne(
            mappedBy = "shop",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private CssSelectorContainerEntity cssSelectorContainer;

    public void setCssSelectorContainer(CssSelectorContainerEntity container) {
        if (container == null) {
            if (this.cssSelectorContainer != null) {
                this.cssSelectorContainer.setShop(null);
            }
        } else {
            container.setShop(this);
        }

        this.cssSelectorContainer = container;
    }

}
