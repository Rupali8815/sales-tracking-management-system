package com.company.salestracker.enums;

/**
 * Well-known seed roles (see database/schema.sql). This enum is a convenience
 * for referencing the default roles in code (e.g. bootstrap seeders, checks);
 * it does NOT constrain the `roles` table, since Admins can create additional
 * custom roles at runtime via RoleService#createRole.
 */
public enum RoleName {
    SUPER_ADMIN,
    ADMIN,
    SALES_MANAGER,
    SALES_EXECUTIVE,
    VIEWER
}
