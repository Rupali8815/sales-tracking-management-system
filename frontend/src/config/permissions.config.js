// Maps a role name to the set of permission codes it is expected to hold.
// This mirrors the backend seed data (database/schema.sql) purely for
// client-side UI gating; the API remains the source of truth via @PreAuthorize.
export const ROLE_PERMISSIONS = {
  SUPER_ADMIN: ['*'],
  ADMIN: [
    'USER_CREATE', 'USER_READ', 'USER_UPDATE', 'USER_DELETE', 'ROLE_MANAGE',
    'LEAD_CREATE', 'LEAD_READ', 'LEAD_UPDATE', 'LEAD_DELETE',
    'DEAL_CREATE', 'DEAL_READ', 'DEAL_UPDATE',
    'SALE_CREATE', 'SALE_READ', 'TARGET_MANAGE', 'REPORT_READ', 'AUDIT_READ',
  ],
  SALES_MANAGER: [
    'LEAD_READ', 'LEAD_UPDATE', 'DEAL_READ', 'DEAL_UPDATE',
    'SALE_READ', 'TARGET_MANAGE', 'REPORT_READ',
  ],
  SALES_EXECUTIVE: [
    'LEAD_CREATE', 'LEAD_READ', 'LEAD_UPDATE',
    'DEAL_CREATE', 'DEAL_READ', 'DEAL_UPDATE',
    'SALE_CREATE', 'SALE_READ',
  ],
  VIEWER: ['LEAD_READ', 'DEAL_READ', 'SALE_READ', 'REPORT_READ'],
};

export const hasPermission = (userRoles = [], permissionCode) => {
  return userRoles.some((role) => {
    const perms = ROLE_PERMISSIONS[role] || [];
    return perms.includes('*') || perms.includes(permissionCode);
  });
};
