package com.zosh.zosh.pos.system.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponValidationResponse {
    private double discountAmount;
    private double finalAmount;
}