import { Routes, Route, Navigate } from 'react-router-dom';

import AuthLayout from '../layouts/AuthLayout';
import AdminLayout from '../layouts/AdminLayout';

import ProtectedRoute from './ProtectedRoute';
import PermissionRoute from './PermissionRoute';

import Login from '../pages/auth/Login';
import ForgotPassword from '../pages/auth/ForgotPassword';
import ResetPassword from '../pages/auth/ResetPassword';

import Dashboard from '../pages/dashboard/Dashboard';
import UserList from '../pages/users/UserList';
import UserForm from '../pages/users/UserForm';
import RoleList from '../pages/roles/RoleList';
import RoleForm from '../pages/roles/RoleForm';
import PermissionList from '../pages/roles/PermissionList';
import LeadList from '../pages/leads/LeadList';
import LeadForm from '../pages/leads/LeadForm';
import LeadDetails from '../pages/leads/LeadDetails';
import DealList from '../pages/deals/DealList';
import DealForm from '../pages/deals/DealForm';
import SalesList from '../pages/sales/SalesList';
import TargetList from '../pages/targets/TargetList';
import SalesReports from '../pages/reports/SalesReports';
import SystemSettings from '../pages/settings/SystemSettings';
import AuditLogs from '../pages/settings/AuditLogs';

export default function AppRoutes() {
  return (
    <Routes>
      {/* Public / auth routes */}
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
      </Route>

      {/* Authenticated app shell */}
      <Route element={<ProtectedRoute />}>
        <Route element={<AdminLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />

          <Route element={<PermissionRoute permission="LEAD_READ" />}>
            <Route path="/leads" element={<LeadList />} />
            <Route path="/leads/new" element={<LeadForm />} />
            <Route path="/leads/:id" element={<LeadDetails />} />
            <Route path="/leads/:id/edit" element={<LeadForm />} />
          </Route>

          <Route element={<PermissionRoute permission="DEAL_READ" />}>
            <Route path="/deals" element={<DealList />} />
            <Route path="/deals/new" element={<DealForm />} />
            <Route path="/deals/:id/edit" element={<DealForm />} />
          </Route>

          <Route element={<PermissionRoute permission="SALE_READ" />}>
            <Route path="/sales" element={<SalesList />} />
          </Route>

          <Route element={<PermissionRoute permission="TARGET_MANAGE" />}>
            <Route path="/targets" element={<TargetList />} />
          </Route>

          <Route element={<PermissionRoute permission="REPORT_READ" />}>
            <Route path="/reports" element={<SalesReports />} />
          </Route>

          <Route element={<PermissionRoute permission="USER_READ" />}>
            <Route path="/users" element={<UserList />} />
            <Route path="/users/new" element={<UserForm />} />
            <Route path="/users/:id/edit" element={<UserForm />} />
          </Route>

          <Route element={<PermissionRoute permission="ROLE_MANAGE" />}>
            <Route path="/roles" element={<RoleList />} />
            <Route path="/roles/new" element={<RoleForm />} />
            <Route path="/roles/:id/edit" element={<RoleForm />} />
            <Route path="/permissions" element={<PermissionList />} />
            <Route path="/settings" element={<SystemSettings />} />
            <Route path="/settings/audit-logs" element={<AuditLogs />} />
          </Route>
        </Route>
      </Route>

      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
