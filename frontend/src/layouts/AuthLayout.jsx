import { Outlet } from 'react-router-dom';

export default function AuthLayout() {
  return (
    <div className="auth-layout">
      <aside className="auth-aside">
        <div className="brand">Sales Tracker</div>
        <div>
          <h2>From first call to paid invoice.</h2>
          <p>Follow every lead through the deal to the sale, and see how each person is doing against their target.</p>
          <div className="pipeline" aria-hidden="true">
            <div className="pipeline-step">Lead<small>Someone interested</small></div>
            <div className="pipeline-step">Deal<small>Price being agreed</small></div>
            <div className="pipeline-step">Sale<small>Invoice and payment</small></div>
          </div>
        </div>
        <span />
      </aside>
      <main className="auth-main">
        <div className="auth-card">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
