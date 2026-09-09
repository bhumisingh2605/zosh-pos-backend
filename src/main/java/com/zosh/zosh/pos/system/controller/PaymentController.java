package com.zosh.zosh.pos.system.controller;

import com.razorpay.Utils;
import com.zosh.zosh.pos.system.payload.dto.RazorpayOrderRequestDto;
import com.zosh.zosh.pos.system.payload.dto.VerifyPaymentRequestDto;
import com.zosh.zosh.pos.system.payload.response.ApiResponse;
import com.zosh.zosh.pos.system.payload.response.RazorpayOrderResponse;
import com.zosh.zosh.pos.system.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/razorpay")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;

    @Value("${razorpay.webhook.secret:}")
    private String webhookSecret;

    // Step 1: cashier's POS screen calls this before opening Razorpay Checkout.
    @PostMapping("/create-order")
    public ResponseEntity<RazorpayOrderResponse> createOrder(
            @RequestBody RazorpayOrderRequestDto request
    ) throws Exception {
        return ResponseEntity.ok(razorpayService.createOrder(request.getAmount()));
    }

    // Step 2 (optional but recommended): frontend can call this right after
    // Checkout succeeds, before it even calls POST /api/orders, so it can show
    // an error immediately if something's wrong. The real enforcement happens
    // again inside OrderServiceImpl.createOrder - never trust this check alone.
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyPayment(
            @RequestBody VerifyPaymentRequestDto request
    ) throws Exception {

        boolean valid = razorpayService.verifySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        ApiResponse response = new ApiResponse();
        response.setMessage(valid ? "Payment verified" : "Payment verification failed");

        if (!valid) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Called directly by Razorpay's servers, not your frontend - there's no JWT
    // on this request, so it's explicitly permitted in SecurityConfig.
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature
    ) {
        try {
            if (webhookSecret == null || webhookSecret.isBlank() || signature == null) {
                return ResponseEntity.ok("ignored");
            }

            boolean valid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
            if (!valid) {
                return ResponseEntity.status(400).body("invalid signature");
            }

            // TODO: parse `payload` as JSON and react to event types such as
            // payment.captured / payment.failed once you need webhook-driven reconciliation.

            return ResponseEntity.ok("received");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("error");
        }
    }
}