package com.company.salestracker.dto.response;

import com.company.salestracker.enums.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AuditLogResponse {
    private Integer id;
    private Integer userId;
    private String userEmail;
    private AuditAction action;
    private String entityName;
    private Integer entityId;
    private LocalDateTime timestamp;
}
