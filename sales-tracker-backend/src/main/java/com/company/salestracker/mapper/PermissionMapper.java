package com.company.salestracker.mapper;

import com.company.salestracker.dto.response.PermissionResponse;
import com.company.salestracker.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    /** DTO fields -> Entity, for creating a brand-new Permission. */
    public Permission toEntity(String code, String description) {
        return Permission.builder()
                .permissionCode(code)
                .description(description)
                .build();
    }

    /** Entity -> DTO, for API responses. */
    public PermissionResponse toResponse(Permission permission) {
        if (permission == null) return null;

        return PermissionResponse.builder()
                .id(permission.getId())
                .permissionCode(permission.getPermissionCode())
                .description(permission.getDescription())
                .build();
    }
}
