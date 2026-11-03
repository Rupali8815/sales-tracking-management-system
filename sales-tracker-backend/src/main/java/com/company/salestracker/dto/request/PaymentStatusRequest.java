package com.company.salestracker.dto.request;

import com.company.salestracker.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentStatusRequest {

    @NotNull(message = "status is required (PENDING, PARTIAL or PAID)")
    private PaymentStatus status;
}
