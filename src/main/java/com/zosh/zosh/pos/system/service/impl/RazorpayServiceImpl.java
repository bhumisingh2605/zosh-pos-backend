package com.zosh.zosh.pos.system.service.impl;

import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.zosh.zosh.pos.system.payload.response.RazorpayOrderResponse;
import com.zosh.zosh.pos.system.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RazorpayServiceImpl implements RazorpayService {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Override
    public RazorpayOrderResponse createOrder(Double amountInRupees) throws Exception {

        if (amountInRupees == null || amountInRupees <= 0) {
            throw new IllegalArgumentException("Order amount must be greater than zero");
        }

        long amountInPaise = Math.round(amountInRupees * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "zosh_" + UUID.randomUUID());
        orderRequest.put("payment_capture", 1);

        // Fully-qualified to avoid clashing with our own modal.Order entity
        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        String razorpayOrderId = razorpayOrder.get("id");

        return RazorpayOrderResponse.builder()
                .razorpayOrderId(razorpayOrderId)
                .amount(amountInPaise)
                .currency("INR")
                .keyId(keyId)
                .build();
    }

    @Override
    public boolean verifySignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) throws Exception {

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", razorpayOrderId);
        options.put("razorpay_payment_id", razorpayPaymentId);
        options.put("razorpay_signature", razorpaySignature);

        return Utils.verifyPaymentSignature(options, keySecret);
    }
}