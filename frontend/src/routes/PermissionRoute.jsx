import { Navigate, Outlet } from 'react-router-dom';
import { usePermission } from '../hooks/usePermission';

export default function PermissionRoute({ permission }) {
  const { can } = usePermission();
  return can(permission) ? <Outlet /> : <Navigate to="/dashboard" replace />;
}
