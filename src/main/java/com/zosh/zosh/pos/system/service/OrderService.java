package com.zosh.zosh.pos.system.service;

import com.zosh.zosh.pos.system.domain.OrderStatus;
import com.zosh.zosh.pos.system.domain.PaymentType;
import com.zosh.zosh.pos.system.payload.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto) throws Exception;
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByBranch(Long branchId,
                                     Long customerId,
                                     Long cashierId,
                                     PaymentType paymentType,
                                     OrderStatus status
    );

    List<OrderDto> getOrderByCashier(Long cashierId);
    void deleteOrder(Long id);
    List<OrderDto> getTodayOrdersByBranch(Long branchId);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    List<OrderDto> getTop5RecentOrdersByBranchId(Long branchId);
}