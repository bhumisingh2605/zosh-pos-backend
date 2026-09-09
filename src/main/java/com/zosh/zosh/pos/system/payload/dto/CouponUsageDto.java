package com.zosh.zosh.pos.system.payload.dto;

import com.zosh.zosh.pos.system.domain.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponUsageDto {
    private Long id;
    private String code;
    private DiscountType discountType;
    private Double discountValue;
    private Boolean active;
    private Integer usageLimit;
    private Integer usedCount;
    private Integer totalOrders;
    private Double totalDiscountGiven;
    private Double totalRevenue; // sum of what customers actually paid on those orders
}