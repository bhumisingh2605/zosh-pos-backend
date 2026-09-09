package com.zosh.zosh.pos.system.payload.dto;

import com.zosh.zosh.pos.system.modal.Customer;
import jakarta.persistence.ManyToOne;
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
public class OrderItemDto {

    private Long id;

    private Integer quantity;

    private Double price;


    private ProductDto product;

    private Long productId;

    private Long orderId;
}
