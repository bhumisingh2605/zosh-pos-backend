package com.zosh.zosh.pos.system.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RazorpayOrderResponse {

    private String razorpayOrderId;
    private Long amount;      // in paise, echoed back so the frontend doesn't have to recompute it
    private String currency;
    private String keyId;     // Razorpay *public* key id - safe to expose to the frontend
}