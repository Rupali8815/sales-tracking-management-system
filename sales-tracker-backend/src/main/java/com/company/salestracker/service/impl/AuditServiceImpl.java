package com.company.salestracker.service.impl;

import com.company.salestracker.entity.AuditLog;
import com.company.salestracker.entity.User;
import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.repository.AuditLogRepository;
import com.company.salestracker.repository.UserRepository;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Override
    public void log(AuditAction action, String entityName, Integer entityId) {
         User user = userRepository.getReferenceById(currentUser.getId());
        logAs(user, action, entityName, entityId);
    }

    @Override
    public void logAs(User user, AuditAction action, String entityName, Integer entityId) {
        auditLogRepository.save(AuditLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .build());
    }
}
