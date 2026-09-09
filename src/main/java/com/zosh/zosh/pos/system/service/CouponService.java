package com.zosh.zosh.pos.system.service;

import com.zosh.zosh.pos.system.modal.Coupon;
import com.zosh.zosh.pos.system.payload.dto.CouponDto;
import com.zosh.zosh.pos.system.payload.dto.CouponUsageDto;

import java.util.List;

public interface CouponService {

    CouponDto createCoupon(CouponDto couponDto) throws Exception;

    CouponDto updateCoupon(Long id, CouponDto couponDto) throws Exception;

    void deleteCoupon(Long id) throws Exception;

    CouponDto getCouponById(Long id) throws Exception;

    List<CouponDto> getCouponsByStore(Long storeId);

    // Validates a coupon against an order total, returns the calculated discount amount
    double validateAndCalculateDiscount(String code, Long storeId, double orderAmount) throws Exception;

    // Increments usedCount — call only after the order is successfully saved
    Coupon incrementUsage(String code, Long storeId) throws Exception;

    List<CouponUsageDto> getCouponUsageReport(Long storeId);
}