package com.zosh.zosh.pos.system.repository;

import com.zosh.zosh.pos.system.modal.OrderItem;
import com.zosh.zosh.pos.system.payload.dto.OrderItemDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
