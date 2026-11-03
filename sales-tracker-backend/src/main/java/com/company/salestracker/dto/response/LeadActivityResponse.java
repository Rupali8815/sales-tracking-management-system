package com.company.salestracker.dto.response;

import com.company.salestracker.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class LeadActivityResponse {
    private Integer id;
    private ActivityType activityType;
    private String notes;
    private String createdByName;
    private LocalDateTime createdAt;
}
