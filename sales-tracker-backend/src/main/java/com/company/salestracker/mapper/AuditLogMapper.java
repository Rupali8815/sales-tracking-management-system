package com.company.salestracker.mapper;

import com.company.salestracker.dto.response.AuditLogResponse;
import com.company.salestracker.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

      public AuditLogResponse toResponse(AuditLog log) {
        if (log == null) return null;

        return AuditLogResponse.builder()
                .id(log.getId())
                .userId(log.getUser() != null ? log.getUser().getId() : null)
                .userEmail(log.getUser() != null ? log.getUser().getEmail() : null)
                .action(log.getAction())
                .entityName(log.getEntityName())
                .entityId(log.getEntityId())
                .timestamp(log.getTimestamp())
                .build();
    }
}
