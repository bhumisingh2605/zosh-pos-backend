package com.zosh.zosh.pos.system.payload.dto;

import lombok.Data;

@Data
public class VerifyPaymentRequestDto {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}