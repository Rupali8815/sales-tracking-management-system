package com.company.salestracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class TargetResponse {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer targetMonth;
    private Integer targetYear;
    private BigDecimal targetAmount;
}
