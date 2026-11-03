package com.company.salestracker.service.impl;

import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.dto.response.RoleResponse;
import com.company.salestracker.entity.Permission;
import com.company.salestracker.entity.Role;
import com.company.salestracker.exception.ResourceNotFoundException;
import com.company.salestracker.mapper.RoleMapper;
import com.company.salestracker.repository.PermissionRepository;
import com.company.salestracker.repository.RoleRepository;
import com.company.salestracker.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final AuditService auditService;

    @Override
    public RoleResponse createRole(String roleName, String description) {
        if (roleRepository.existsByRoleName(roleName)) {
            throw new IllegalArgumentException("A role with this name already exists: " + roleName);
        }

        Role role = roleMapper.toEntity(roleName, description);
        Role saved = roleRepository.save(role);
        auditService.log(AuditAction.CREATE, "Role", saved.getId());
        return roleMapper.toResponse(saved);
    }

    @Override
    public RoleResponse assignPermissions(Integer roleId, Set<Integer> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.setPermissions(new HashSet<>(permissions));

        Role saved = roleRepository.save(role);
        auditService.log(AuditAction.UPDATE, "Role", saved.getId());
        return roleMapper.toResponse(saved);
    }

    @Override
    public List<RoleResponse> listRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
    }
}
