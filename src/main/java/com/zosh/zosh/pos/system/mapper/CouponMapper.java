package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.Coupon;
import com.zosh.zosh.pos.system.payload.dto.CouponDto;

public class CouponMapper {

    public static CouponDto toDTO(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minOrderAmount(coupon.getMinOrderAmount())
                .maxDiscountAmount(coupon.getMaxDiscountAmount())
                .usageLimit(coupon.getUsageLimit())
                .usedCount(coupon.getUsedCount())
                .expiryDate(coupon.getExpiryDate())
                .active(coupon.getActive())
                .storeId(coupon.getStore() != null ? coupon.getStore().getId() : null)
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}