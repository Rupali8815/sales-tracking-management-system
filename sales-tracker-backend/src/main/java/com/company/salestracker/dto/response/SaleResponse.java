package com.company.salestracker.dto.response;

import com.company.salestracker.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class SaleResponse {
    private Integer id;
    private Integer dealId;
    private String leadName;
    private String assignedToName;
    private BigDecimal saleAmount;
    private PaymentStatus paymentStatus;
    private String invoiceNumber;
    private LocalDate saleDate;
}
