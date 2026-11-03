package com.company.salestracker.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class RolePermissionsRequest {

    /** IDs of the permissions this role should hold (replaces the existing set). */
    @NotNull(message = "permissionIds is required")
    private Set<Integer> permissionIds;
}
