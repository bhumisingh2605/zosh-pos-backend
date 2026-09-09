package com.zosh.zosh.pos.system.controller;

import com.zosh.zosh.pos.system.payload.dto.CouponDto;
import com.zosh.zosh.pos.system.payload.dto.CouponUsageDto;
import com.zosh.zosh.pos.system.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<CouponDto> createCoupon(@RequestBody CouponDto couponDto) throws Exception {
        return ResponseEntity.ok(couponService.createCoupon(couponDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<CouponDto> updateCoupon(
            @PathVariable Long id,
            @RequestBody CouponDto couponDto
    ) throws Exception {
        return ResponseEntity.ok(couponService.updateCoupon(id, couponDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDto> getCouponById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CouponDto>> getCouponsByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(couponService.getCouponsByStore(storeId));
    }
    @GetMapping("/store/{storeId}/report")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<List<CouponUsageDto>> getCouponUsageReport(@PathVariable Long storeId) {
        return ResponseEntity.ok(couponService.getCouponUsageReport(storeId));
    }
}