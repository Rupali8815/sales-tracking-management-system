package com.company.salestracker.dto.response;

import com.company.salestracker.enums.LeadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class LeadResponse {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String source;
    private LeadStatus status;
    private Integer assignedTo;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean editable;
}
