package com.example.price_monitoring_system.repository.entity;

import com.example.price_monitoring_system.domain.Product;
import com.example.price_monitoring_system.domain.Shop;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tracked_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackedItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tracked_item_seq_generator")
    @SequenceGenerator(
            name = "tracked_item_seq_generator",
            sequenceName = "tracked_item_seq"
    )
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @UpdateTimestamp
    @Column(name = "last_checked", nullable = false)
    private LocalDateTime lastChecked;

    @
    private List<Long> listenerIds;
}
