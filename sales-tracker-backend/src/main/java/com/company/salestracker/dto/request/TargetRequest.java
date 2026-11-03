package com.company.salestracker.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TargetRequest {

    @NotNull(message = "userId is required")
    private Integer userId;

    @NotNull(message = "month is required")
    @Min(value = 1, message = "month must be 1-12")
    @Max(value = 12, message = "month must be 1-12")
    private Integer month;

    @NotNull(message = "year is required")
    @Min(value = 2000, message = "year looks invalid")
    @Max(value = 2100, message = "year looks invalid")
    private Integer year;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;
}
