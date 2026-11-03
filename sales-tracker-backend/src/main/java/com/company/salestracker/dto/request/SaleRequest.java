package com.company.salestracker.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaleRequest {

    @NotNull(message = "dealId is required")
    private Integer dealId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 100)
    private String invoiceNumber;

    /** Optional; defaults to today when null. */
    private LocalDate saleDate;
}
