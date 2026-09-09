package com.zosh.zosh.pos.system.modal;

import com.zosh.zosh.pos.system.domain.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Double discountValue;      // e.g. 10 (%) or 100 (flat currency amount)

    private Double minOrderAmount;     // minimum cart total required to use coupon

    private Double maxDiscountAmount;  // cap on discount for PERCENTAGE coupons (nullable)

    private Integer usageLimit;        // total times coupon can be used (nullable = unlimited)

    private Integer usedCount;

    private LocalDateTime expiryDate;

    private Boolean active;

    @ManyToOne
    private Store store;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (usedCount == null) usedCount = 0;
        if (active == null) active = true;
    }
}