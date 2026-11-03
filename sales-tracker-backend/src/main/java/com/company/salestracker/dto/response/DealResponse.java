package com.company.salestracker.dto.response;

import com.company.salestracker.enums.DealStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DealResponse {
    private Integer id;
    private Integer leadId;
    private String leadName;
    private DealStage dealStage;
    private BigDecimal expectedAmount;
    private LocalDate closingDate;
    private Integer assignedTo;
    private String assignedToName;
    private LocalDateTime createdAt;
    private boolean editable;
}
