package com.company.salestracker.controller;

import com.company.salestracker.dto.response.AuditLogResponse;
import com.company.salestracker.entity.AuditLog;
import com.company.salestracker.mapper.AuditLogMapper;
import com.company.salestracker.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUDIT_READ')")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @GetMapping
    public ResponseEntity<Page<AuditLogResponse>> list(@RequestParam(required = false) String entityName,
            @org.springframework.data.web.PageableDefault(sort = "timestamp", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<AuditLog> page = (entityName != null && !entityName.isBlank())
                ? auditLogRepository.findByEntityName(entityName, pageable)
                : auditLogRepository.findAll(pageable);
        return ResponseEntity.ok(page.map(auditLogMapper::toResponse));
    }
}
