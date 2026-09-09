package com.zosh.zosh.pos.system.mapper;

import com.zosh.zosh.pos.system.modal.Refund;
import com.zosh.zosh.pos.system.payload.dto.RefundDto;

public class RefundMapper {

    public static RefundDto toDTO(Refund refund) {

        return RefundDto.builder()
                .id(refund.getId())
                .orderId(refund.getOrder().getId())
                .reason(refund.getReason())
                .amount(refund.getAmount())
                .cashier(UserMapper.toDTO(refund.getCashier()))
                .branchId(refund.getBranch().getId())
                .shiftReportId(refund.getShiftReport()!= null?refund.getShiftReport().getId():null)
                .paymentType(refund.getPaymentType())
                .createdAt(refund.getCreatedAt())
                .build();
    }
}