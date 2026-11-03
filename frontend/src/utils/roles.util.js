// Record-level rules mirror the backend (CurrentUser). The backend is the real gatekeeper;
// these only decide which buttons/fields to show.
const MANAGER_ROLES = ['SUPER_ADMIN', 'ADMIN', 'SALES_MANAGER'];

export const hasAnyRole = (user, names) => (user?.roles || []).some((r) => names.includes(r));

/** Admin / Super Admin / Sales Manager: sees everything, may re-assign. */
export const isManagerOrAbove = (user) => hasAnyRole(user, MANAGER_ROLES);

/** Sales Executive without a wider role: only own leads/deals. */
export const isOwnerScoped = (user) => !isManagerOrAbove(user) && !hasAnyRole(user, ['VIEWER']);
