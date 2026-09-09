package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.Order;
import com.zosh.zosh.pos.system.modal.Product;
import com.zosh.zosh.pos.system.modal.Refund;
import com.zosh.zosh.pos.system.modal.ShiftReport;
import com.zosh.zosh.pos.system.payload.dto.OrderDto;
import com.zosh.zosh.pos.system.payload.dto.ProductDto;
import com.zosh.zosh.pos.system.payload.dto.RefundDto;
import com.zosh.zosh.pos.system.payload.dto.ShiftReportDto;

import java.util.List;
import java.util.stream.Collectors;

public class ShiftReportMapper {

    public ShiftReportMapper() {
        super();
    }

    public static ShiftReportDto toDTO(ShiftReport entity) {
        return ShiftReportDto.builder()
                .id(entity.getId())
                .shiftEnd(entity.getShiftEnd())
                .shiftStart(entity.getShiftStart())
                .totalSales(entity.getTotalSales())
                .totalOrders(entity.getTotalOrders())
                .totalRefunds(entity.getTotalRefunds())
                .netSale(entity.getNetSale())
                .cashier(entity.getCashier() != null ? UserMapper.toDTO(entity.getCashier()) : null)
                .cashierId(entity.getCashier() != null ? entity.getCashier().getId() : null)
                .branchId(entity.getBranch() != null ? entity.getBranch().getId() : null)
                .recentOrders(mapOrders(entity.getRecentOrders()))
                .topSellingProducts(mapProducts(entity.getTopSellingProducts()))
                .refunds(mapRefunds(entity.getRefunds()))
                .paymentSummaries(entity.getPaymentSummaries())
                .build();
    }

    private static List<RefundDto> mapRefunds(List<Refund> refunds) {
        if (refunds == null || refunds.isEmpty()) {
            return null;
        }
        return refunds.stream().map(RefundMapper::toDTO).collect(Collectors.toList());
    }

    private static List<ProductDto> mapProducts(List<Product> topSellingProducts) {
        if (topSellingProducts == null || topSellingProducts.isEmpty()) {
            return null;
        }
        return topSellingProducts.stream().map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    private static List<OrderDto> mapOrders(List<Order> recentOrders) {
        if (recentOrders == null || recentOrders.isEmpty()) {
            return null;
        }
        return recentOrders.stream()
                .map(OrderMapper::toDTO).collect(Collectors.toList());
    }

}