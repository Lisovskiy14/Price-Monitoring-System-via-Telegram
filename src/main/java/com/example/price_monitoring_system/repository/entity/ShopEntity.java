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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shops_seq_generator")
    @SequenceGenerator(
            name = "shops_seq_generator",
            sequenceName = "shops_seq"
    )
    private Long id;

    @Column(name = "domain", nullable = false, unique = true)
    private String domain;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "css_selector_container_id")
    private CssSelectorContainerEntity cssSelectorContainer;

}
