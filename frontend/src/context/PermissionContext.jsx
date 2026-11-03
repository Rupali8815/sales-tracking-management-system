import { createContext, useContext, useMemo } from 'react';
import { AuthContext } from './AuthContext';
import { hasPermission } from '../config/permissions.config';

export const PermissionContext = createContext({ can: () => false });

export function PermissionProvider({ children }) {
  const { user } = useContext(AuthContext) || {};

  const value = useMemo(
    () => ({
      can: (permissionCode) => hasPermission(user?.roles || [], permissionCode),
      roles: user?.roles || [],
    }),
    [user]
  );

  return <PermissionContext.Provider value={value}>{children}</PermissionContext.Provider>;
}
