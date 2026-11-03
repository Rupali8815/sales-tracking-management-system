package com.company.salestracker.service;

import com.company.salestracker.entity.User;
import com.company.salestracker.enums.AuditAction;
public interface AuditService {
     void log(AuditAction action, String entityName, Integer entityId);

    void logAs(User user, AuditAction action, String entityName, Integer entityId);
}
