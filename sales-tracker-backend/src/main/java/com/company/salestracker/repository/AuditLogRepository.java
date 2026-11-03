package com.company.salestracker.repository;

import com.company.salestracker.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
    Page<AuditLog> findByEntityName(String entityName, Pageable pageable);
}
