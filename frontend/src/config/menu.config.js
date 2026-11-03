export const MENU_ITEMS = [
  { label: 'Dashboard', path: '/dashboard', permission: null },
  { label: 'Leads', path: '/leads', permission: 'LEAD_READ' },
  { label: 'Deals', path: '/deals', permission: 'DEAL_READ' },
  { label: 'Sales', path: '/sales', permission: 'SALE_READ' },
  { label: 'Targets', path: '/targets', permission: 'TARGET_MANAGE' },
  { label: 'Reports', path: '/reports', permission: 'REPORT_READ' },
  { label: 'Users', path: '/users', permission: 'USER_READ' },
  { label: 'Roles', path: '/roles', permission: 'ROLE_MANAGE' },
  { label: 'Permissions', path: '/permissions', permission: 'ROLE_MANAGE' },
  { label: 'Settings', path: '/settings', permission: 'ROLE_MANAGE' },
];
