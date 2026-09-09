package com.zosh.zosh.pos.system.service;

import com.zosh.zosh.pos.system.modal.Refund;
import com.zosh.zosh.pos.system.payload.dto.RefundDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundService {

    RefundDto createRefund(RefundDto refund) throws Exception;
    List<RefundDto> getAllRefunds() throws Exception;
    List<RefundDto> getRefundByCashier(Long cashierId) throws Exception;
    List<RefundDto> getRefundByShiftReport(Long shiftReportId) throws Exception;
    List<RefundDto> getRefundByCashierAndDateRange(Long cashierId,
                                                   LocalDateTime startDate, LocalDateTime endDate);
    List<RefundDto> getRefundByBranch(Long branchId) throws Exception;

    RefundDto getRefundById(Long refundId) throws Exception;

    void deleteRefund(Long refundId) throws Exception;





}
