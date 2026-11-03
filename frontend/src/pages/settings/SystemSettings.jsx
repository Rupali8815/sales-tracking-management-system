import { Link } from 'react-router-dom';

export default function SystemSettings() {
  return (
    <div>
      <h1 className="page-title">System Settings</h1>
      <div className="card">
        <h2 className="card-title">Administration</h2>
        <ul className="settings-list">
          <li><Link to="/roles">Manage Roles & Permissions</Link></li>
          <li><Link to="/settings/audit-logs">View Audit Logs</Link></li>
          <li><Link to="/users">Manage Users</Link></li>
        </ul>
      </div>
      <div className="card">
        <h2 className="card-title">About</h2>
        <p className="muted">Sales Tracking & Management System — RBAC-enabled, monolithic Spring Boot backend with a React frontend.</p>
      </div>
    </div>
  );
}
