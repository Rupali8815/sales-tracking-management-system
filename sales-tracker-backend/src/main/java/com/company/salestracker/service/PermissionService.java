package com.company.salestracker.service;

import com.company.salestracker.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(String code, String description);
    List<PermissionResponse> listPermissions();
}
