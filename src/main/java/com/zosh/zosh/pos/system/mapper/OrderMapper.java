package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.Order;
import com.zosh.zosh.pos.system.payload.dto.OrderDto;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto toDTO(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .branchId(order.getBranch().getId())
                .cashier(UserMapper.toDTO(order.getCashier()))
                .customer(order.getCustomer())
                .paymentType(order.getPaymentType())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(OrderItemMapper::toDTO)
                        .collect(Collectors.toList()))
                .couponCode(order.getCouponCode())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .razorpayOrderId(order.getRazorpayOrderId())
                .razorpayPaymentId(order.getRazorpayPaymentId())
                .build();

    }
}
