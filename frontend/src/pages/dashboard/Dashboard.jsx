import { useEffect, useState } from 'react';
import * as salesApi from '../../api/sales.api';
import * as reportsApi from '../../api/reports.api';
import SalesLineChart from '../../components/charts/SalesLineChart';
import Loader from '../../components/common/Loader';
import { usePermission } from '../../hooks/usePermission';
import { currentMonthYear } from '../../utils/date.util';
import { formatINR, toNumber } from '../../utils/error.util';

export default function Dashboard() {
  const { can } = usePermission();
  const [loading, setLoading] = useState(true);
  const [monthlyTotal, setMonthlyTotal] = useState(0);
  const [yearlyTotal, setYearlyTotal] = useState(0);
  const [conversionRatio, setConversionRatio] = useState(0);
  const [periodData, setPeriodData] = useState([]);

  const canSales = can('SALE_READ');
  const canReports = can('REPORT_READ');

  useEffect(() => {
    const { month, year } = currentMonthYear();
    const skip = Promise.resolve({ data: null });

    // allSettled: one forbidden/failed call must not break the whole dashboard
    Promise.allSettled([
      canSales ? salesApi.monthlySummary(month, year) : skip,
      canSales ? salesApi.yearlySummary(year) : skip,
      canReports ? reportsApi.conversionRatio() : skip,
      canReports ? reportsApi.salesByPeriod(year) : skip,
    ])
      .then(([monthly, yearly, ratio, period]) => {
        const val = (res) => (res.status === 'fulfilled' ? res.value.data : null);
        setMonthlyTotal(toNumber(val(monthly)));
        setYearlyTotal(toNumber(val(yearly)));
        setConversionRatio(toNumber(val(ratio)));
        const rows = val(period);
        setPeriodData(Array.isArray(rows) ? rows : []);
      })
      .finally(() => setLoading(false));
  }, [canSales, canReports]);

  if (loading) return <Loader label="Loading dashboard..." />;

  return (
    <div>
      <h1 className="page-title">Dashboard</h1>

      {!canSales && !canReports && (
        <div className="card">
          <p className="muted">Welcome! Use the menu on the left to get started.</p>
        </div>
      )}

      {canSales && (
        <div className="stat-grid">
          <div className="stat-card">
            <span className="stat-label">This Month's Sales</span>
            <span className="stat-value">{formatINR(monthlyTotal)}</span>
          </div>
          <div className="stat-card">
            <span className="stat-label">This Year's Sales</span>
            <span className="stat-value">{formatINR(yearlyTotal)}</span>
          </div>
          {canReports && (
            <div className="stat-card">
              <span className="stat-label">Lead &rarr; Deal Conversion</span>
              <span className="stat-value">{conversionRatio.toFixed(1)}%</span>
            </div>
          )}
        </div>
      )}

      {canReports && (
        <div className="card">
          <h2 className="card-title">Sales by Month</h2>
          <SalesLineChart data={periodData} xKey="month" yKey="total" />
        </div>
      )}
    </div>
  );
}
