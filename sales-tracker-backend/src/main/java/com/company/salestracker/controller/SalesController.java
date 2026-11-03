package com.company.salestracker.controller;

import com.company.salestracker.dto.request.PaymentStatusRequest;
import com.company.salestracker.dto.request.SaleRequest;
import jakarta.validation.Valid;
import com.company.salestracker.dto.response.SaleResponse;
import com.company.salestracker.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @PostMapping
    @PreAuthorize("hasAuthority('SALE_CREATE')")
    public ResponseEntity<SaleResponse> recordSale(@Valid @RequestBody SaleRequest request) {
        return ResponseEntity.ok(salesService.recordSale(
                request.getDealId(), request.getAmount(), request.getInvoiceNumber(), request.getSaleDate()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SALE_READ')")
    public ResponseEntity<Page<SaleResponse>> list(
            @PageableDefault(sort = "saleDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(salesService.listSales(pageable));
    }

    @PutMapping("/{id}/payment-status")
    @PreAuthorize("hasAuthority('SALE_CREATE')")
    public ResponseEntity<SaleResponse> updatePaymentStatus(@PathVariable Integer id, @Valid @RequestBody PaymentStatusRequest request) {
        return ResponseEntity.ok(salesService.updatePaymentStatus(id, request.getStatus().name()));
    }

    @GetMapping("/summary/monthly")
    @PreAuthorize("hasAuthority('SALE_READ')")
    public ResponseEntity<BigDecimal> monthlySummary(@RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(salesService.getMonthlySalesTotal(month, year));
    }

    @GetMapping("/summary/yearly")
    @PreAuthorize("hasAuthority('SALE_READ')")
    public ResponseEntity<BigDecimal> yearlySummary(@RequestParam int year) {
        return ResponseEntity.ok(salesService.getYearlySalesTotal(year));
    }
}
