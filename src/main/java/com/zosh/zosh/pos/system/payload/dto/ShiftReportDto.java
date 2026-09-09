package com.zosh.zosh.pos.system.payload.dto;

import com.zosh.zosh.pos.system.modal.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ShiftReportDto {

    private Long id;

    private LocalDateTime shiftStart;
    private  LocalDateTime shiftEnd;

    private Double totalSales;
    private Double totalRefunds;
    private Double netSale;
    private int  totalOrders;

    private UserDto cashier;
    private Long cashierId;
    private Long branchId;

    private BranchDto branch;

    private List<PaymentSummary> paymentSummaries;

    private List<ProductDto> topSellingProducts;

    private List<OrderDto> recentOrders;

    private List<RefundDto> refunds;

}
