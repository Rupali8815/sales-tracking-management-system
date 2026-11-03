import { useEffect, useState } from 'react';
import * as reportsApi from '../../api/reports.api';
import SalesBarChart from '../../components/charts/SalesBarChart';
import SalesPieChart from '../../components/charts/SalesPieChart';
import DataTable from '../../components/tables/DataTable';
import Loader from '../../components/common/Loader';
import { formatINR, toNumber } from '../../utils/error.util';

export default function SalesReports() {
  const [loading, setLoading] = useState(true);
  const [periodData, setPeriodData] = useState([]);
  const [userSalesData, setUserSalesData] = useState([]);
  const [conversionRatio, setConversionRatio] = useState(0);
  const [lostDeals, setLostDeals] = useState([]);

  useEffect(() => {
    const year = new Date().getFullYear();
    const month = new Date().getMonth() + 1;

    Promise.allSettled([
      reportsApi.salesByPeriod(year),
      reportsApi.salesByUser(month, year),
      reportsApi.conversionRatio(),
      reportsApi.lostDeals(),
    ]).then(([period, byUser, ratio, lost]) => {
      const val = (res) => (res.status === 'fulfilled' ? res.value.data : null);
      const p = val(period);
      const u = val(byUser);
      const l = val(lost);
      setPeriodData(Array.isArray(p) ? p : []);
      setUserSalesData(u && typeof u === 'object' ? Object.entries(u).map(([name, value]) => ({ name, value: toNumber(value) })) : []);
      setConversionRatio(toNumber(val(ratio)));
      setLostDeals(Array.isArray(l) ? l : []);
    }).finally(() => setLoading(false));
  }, []);

  const lostColumns = [
    { key: 'leadName', header: 'Lead' },
    { key: 'expectedAmount', header: 'Expected Amount', render: (row) => formatINR(row.expectedAmount) },
    { key: 'assignedTo', header: 'Assigned To' },
    { key: 'closingDate', header: 'Closing Date' },
  ];

  if (loading) return <Loader label="Building reports..." />;

  return (
    <div>
      <h1 className="page-title">Sales Reports & Analytics</h1>

      <div className="card">
        <h2 className="card-title">Sales by Month</h2>
        <SalesBarChart data={periodData} xKey="month" yKey="total" />
      </div>

      <div className="card">
        <h2 className="card-title">Sales by User (This Month)</h2>
        {userSalesData.length > 0
          ? <SalesPieChart data={userSalesData} />
          : <p className="muted">No sales recorded yet this month.</p>}
      </div>

      <div className="card">
        <h2 className="card-title">Lead → Deal Conversion Ratio</h2>
        <p className="stat-value">{conversionRatio.toFixed(1)}%</p>
      </div>

      <div className="card">
        <h2 className="card-title">Lost Deal Analysis</h2>
        <DataTable columns={lostColumns} rows={lostDeals} loading={false} emptyMessage="No lost deals recorded." />
      </div>
    </div>
  );
}
