package com.zosh.zosh.pos.system.controller;

import com.zosh.zosh.pos.system.modal.Refund;
import com.zosh.zosh.pos.system.payload.dto.RefundDto;
import com.zosh.zosh.pos.system.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundDto> createRefund(@RequestBody RefundDto refundDto) throws Exception {
        RefundDto refund = refundService.createRefund(refundDto);
        return ResponseEntity.ok(refund);
    }

    @GetMapping
    public ResponseEntity<List<RefundDto>> getAllRefund() throws Exception {
        List<RefundDto> refund = refundService.getAllRefunds();
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<RefundDto>> getRefundByCashier(
            @PathVariable Long cashierId
    ) throws Exception {
        List<RefundDto> refund = refundService.getRefundByCashier(cashierId);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RefundDto>> getRefundByBranch(
            @PathVariable Long branchId
    ) throws Exception {
        List<RefundDto> refund = refundService.getRefundByCashier(branchId);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/shift/{shiftId}")
    public ResponseEntity<List<RefundDto>> getRefundByShift(
            @PathVariable Long shiftId
    ) throws Exception {
        List<RefundDto> refund = refundService.getRefundByShiftReport(shiftId);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/cashier/{cashierId}/range")
    public ResponseEntity<List<RefundDto>> getRefundByCashierAndDateRange(
            @PathVariable Long cashierId,
            @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
            ) throws Exception {
        List<RefundDto> refund = refundService.getRefundByCashierAndDateRange(
                cashierId,
                startDate, endDate
        );
        return ResponseEntity.ok(refund);
    }


    @GetMapping("/{id}")
    public ResponseEntity <RefundDto> getRefundById(
            @PathVariable Long id
    ) throws Exception {
        RefundDto refund = refundService.getRefundById(id);
        return ResponseEntity.ok(refund);
    }


}
