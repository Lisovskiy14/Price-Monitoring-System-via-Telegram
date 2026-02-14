package com.example.price_monitoring_system.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_snapshots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_snapshots_seq_generator")
    @SequenceGenerator(
            name = "item_snapshots_seq_generator",
            sequenceName = "item_snapshots_seq"
    )
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "tracked_item_id")
    private TrackedItemEntity trackedItem;

    @Column(name = "previous_price", nullable = false)
    private BigDecimal previousPrice;

    @Column(name = "previous_available", nullable = false)
    private boolean previousAvailable;

    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;
}
