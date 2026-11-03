import { Outlet, NavLink } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { usePermission } from '../hooks/usePermission';
import { MENU_ITEMS } from '../config/menu.config';

export default function AdminLayout() {
  const { user, logout } = useAuth();
  const { can } = usePermission();

  const visibleItems = MENU_ITEMS.filter((item) => !item.permission || can(item.permission));

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="sidebar-brand">Sales Tracker</div>
        <nav className="sidebar-nav">
          {visibleItems.map((item) => (
            <NavLink key={item.path} to={item.path} className="sidebar-link">
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>
      <div className="main-column">
        <header className="topbar">
          <span className="topbar-user">
            <span className="avatar" aria-hidden="true">{(user?.name || 'U').trim().charAt(0).toUpperCase()}</span>
            {user?.name || 'Admin'}
          </span>
          <button className="link-btn" onClick={logout}>Logout</button>
        </header>
        <main className="page-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
