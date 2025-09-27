package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.dto.MonthlyTransactionReportDTO;
import com.example.banking.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/generate-report")
    public ResponseEntity<ApiResponse<List<MonthlyTransactionReportDTO>>> getMonthlyReport(
            @RequestParam int month,
            @RequestParam int year) {
        try {
            List<MonthlyTransactionReportDTO> report = reportService.getMonthlyReport(month, year);
            ApiResponse<List<MonthlyTransactionReportDTO>> response = ApiResponse.<List<MonthlyTransactionReportDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Monthly transaction report fetched successfully")
                    .data(report)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ApiResponse<List<MonthlyTransactionReportDTO>> errorResponse = ApiResponse.<List<MonthlyTransactionReportDTO>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch report: " + ex.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

