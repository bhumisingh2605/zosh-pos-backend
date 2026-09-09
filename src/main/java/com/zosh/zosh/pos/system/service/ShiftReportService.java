package com.zosh.zosh.pos.system.service;

import com.zosh.zosh.pos.system.exceptions.UserException;
import com.zosh.zosh.pos.system.modal.ShiftReport;
import com.zosh.zosh.pos.system.payload.dto.ShiftReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftReportService {

    ShiftReportDto startShift() throws Exception;

    ShiftReportDto endShift(Long shiftReportId, LocalDateTime shiftEnd) throws Exception;

    ShiftReportDto getShiftReportById(Long id) throws Exception;

    List<ShiftReportDto> getAllShiftReports();

    List<ShiftReportDto> getShiftReportsByBranchId(Long branchId);
    List<ShiftReportDto> getShiftReportsByCashierId(Long cashierId);

    ShiftReportDto getCurrentShiftProgress(Long cashierId) throws Exception;

    ShiftReportDto getShiftByCashierAndDate(Long cashierId, LocalDateTime date) throws Exception;





}
