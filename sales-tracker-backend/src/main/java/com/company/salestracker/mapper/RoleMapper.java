package com.company.salestracker.mapper;

import com.company.salestracker.dto.response.RoleResponse;
import com.company.salestracker.entity.Permission;
import com.company.salestracker.entity.Role;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    /** DTO fields -> Entity, for creating a brand-new Role. */
    public Role toEntity(String roleName, String description) {
        return Role.builder()
                .roleName(roleName)
                .description(description)
                .permissions(new HashSet<>())
                .build();
    }

    /** Entity -> DTO, for API responses. */
    public RoleResponse toResponse(Role role) {
        if (role == null) return null;

        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .permissions(role.getPermissions().stream()
                        .map(Permission::getPermissionCode)
                        .collect(Collectors.toSet()))
                .createdAt(role.getCreatedAt())
                .build();
    }
}
