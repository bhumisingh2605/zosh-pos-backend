package com.zosh.zosh.pos.system.service;

import com.zosh.zosh.pos.system.payload.response.RazorpayOrderResponse;

public interface RazorpayService {

    RazorpayOrderResponse createOrder(Double amountInRupees) throws Exception;

    boolean verifySignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) throws Exception;
}