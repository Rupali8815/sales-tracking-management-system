package com.company.salestracker.service.impl;

import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.dto.response.PermissionResponse;
import com.company.salestracker.entity.Permission;
import com.company.salestracker.mapper.PermissionMapper;
import com.company.salestracker.repository.PermissionRepository;
import com.company.salestracker.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final AuditService auditService;

    @Override
    public PermissionResponse createPermission(String code, String description) {
        if (permissionRepository.findByPermissionCode(code).isPresent()) {
            throw new IllegalArgumentException("A permission with this code already exists: " + code);
        }

        Permission permission = permissionMapper.toEntity(code, description);
        Permission saved = permissionRepository.save(permission);
        auditService.log(AuditAction.CREATE, "Permission", saved.getId());
        return permissionMapper.toResponse(saved);
    }

    @Override
    public List<PermissionResponse> listPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
