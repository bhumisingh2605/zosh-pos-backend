package com.zosh.zosh.pos.system.payload.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zosh.zosh.pos.system.domain.PaymentType;
import com.zosh.zosh.pos.system.modal.Branch;
import com.zosh.zosh.pos.system.modal.Order;
import com.zosh.zosh.pos.system.modal.ShiftReport;
import com.zosh.zosh.pos.system.modal.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundDto {

    private Long id;

    private OrderDto order;
    private Long orderId;

    private String reason;

    private Double amount;

  //  private ShiftReport shiftReport;
    private Long shiftReportId;

    private UserDto cashier;

    private BranchDto branch;
    private Long branchId;


    private PaymentType paymentType;
    private LocalDateTime createdAt;



}
