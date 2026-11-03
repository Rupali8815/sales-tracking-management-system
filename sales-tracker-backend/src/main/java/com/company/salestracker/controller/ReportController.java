package com.company.salestracker.controller;

import com.company.salestracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('REPORT_READ')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales-by-user")
    public ResponseEntity<Map<String, BigDecimal>> salesByUser(@RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(reportService.salesByUser(month, year));
    }

    @GetMapping("/sales-by-period")
    public ResponseEntity<List<Map<String, Object>>> salesByPeriod(@RequestParam int year) {
        return ResponseEntity.ok(reportService.salesByTimePeriod(year));
    }

    @GetMapping("/conversion-ratio")
    public ResponseEntity<Double> conversionRatio() {
        return ResponseEntity.ok(reportService.leadToDealConversionRatio());
    }

    @GetMapping("/lost-deals")
    public ResponseEntity<List<Map<String, Object>>> lostDeals() {
        return ResponseEntity.ok(reportService.lostDealAnalysis());
    }
}
