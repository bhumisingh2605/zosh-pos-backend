package com.zosh.zosh.pos.system.payload.dto;

import lombok.Data;

@Data
public class RazorpayOrderRequestDto {

    // Amount in rupees (e.g. 499.00) - converted to paise before calling Razorpay
    private Double amount;
}