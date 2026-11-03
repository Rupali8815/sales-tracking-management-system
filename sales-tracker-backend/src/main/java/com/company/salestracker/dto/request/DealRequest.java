package com.company.salestracker.dto.request;

import com.company.salestracker.enums.DealStage;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DealRequest {

    private Integer leadId;
    private DealStage dealStage;
    private BigDecimal expectedAmount;
    private LocalDate closingDate;
    private Integer assignedTo;
}
