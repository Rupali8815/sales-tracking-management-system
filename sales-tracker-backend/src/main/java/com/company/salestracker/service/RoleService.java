package com.company.salestracker.service;

import com.company.salestracker.dto.response.RoleResponse;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleResponse createRole(String roleName, String description);
    RoleResponse assignPermissions(Integer roleId, Set<Integer> permissionIds);
    List<RoleResponse> listRoles();
}
