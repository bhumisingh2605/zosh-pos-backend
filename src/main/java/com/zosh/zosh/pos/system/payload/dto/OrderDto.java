package com.zosh.zosh.pos.system.payload.dto;

import com.zosh.zosh.pos.system.domain.PaymentType;
import com.zosh.zosh.pos.system.modal.Branch;
import com.zosh.zosh.pos.system.modal.Customer;
import com.zosh.zosh.pos.system.modal.OrderItem;
import com.zosh.zosh.pos.system.modal.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private Double totalAmount;

    private LocalDateTime createdAt;

    private Long branchId;
    private Long customerId;

    private BranchDto branch;

    private UserDto cashier;

    private Customer customer;

    private PaymentType paymentType;

    private List<OrderItemDto> items;

    private String couponCode;

    private Double discountAmount;

    private Double finalAmount;

    // Populated by the frontend after Razorpay Checkout succeeds, for UPI/CARD
    // orders. Required (and re-verified server-side) when paymentType != CASH.
    private String razorpayOrderId;
    private String razorpayPaymentId;

    // Only used as verification input during order creation - never persisted.
    private String razorpaySignature;
}
