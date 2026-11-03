import { useContext } from 'react';
import { PermissionContext } from '../context/PermissionContext';

export function usePermission() {
  return useContext(PermissionContext);
}
