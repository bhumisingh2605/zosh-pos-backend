package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.OrderItem;
import com.zosh.zosh.pos.system.payload.dto.OrderItemDto;

public class OrderItemMapper {

    public static OrderItemDto toDTO(OrderItem item) {

        if(item == null) return null;
        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())

                .quantity(item.getQuantity())
                .price(item.getPrice())
                .product(ProductMapper.toDTO(item.getProduct()))
                .build();
    }
}
