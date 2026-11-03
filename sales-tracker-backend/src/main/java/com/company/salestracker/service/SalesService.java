package com.company.salestracker.service;

import com.company.salestracker.dto.response.SaleResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalesService {
    SaleResponse recordSale(Integer dealId, BigDecimal amount, String invoiceNumber, LocalDate saleDate);
    Page<SaleResponse> listSales(Pageable pageable);
    SaleResponse updatePaymentStatus(Integer saleId, String paymentStatus);
    BigDecimal getMonthlySalesTotal(int month, int year);
    BigDecimal getYearlySalesTotal(int year);
}
