import { useEffect, useState, useCallback } from 'react';
import * as salesApi from '../../api/sales.api';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Button from '../../components/common/Button';
import DataTable from '../../components/tables/DataTable';
import Pagination from '../../components/tables/Pagination';
import { usePermission } from '../../hooks/usePermission';
import { usePagination } from '../../hooks/usePagination';
import { currentMonthYear, monthName, formatDate } from '../../utils/date.util';
import { PAYMENT_STATUSES } from '../../utils/constants';
import { getErrorMessage, formatINR, toNumber } from '../../utils/error.util';

const MONTHS = Array.from({ length: 12 }, (_, i) => ({ value: i + 1, label: monthName(i + 1) }));

export default function SalesList() {
  const { can } = usePermission();
  const now = currentMonthYear();
  const { page, size, setPage } = usePagination();

  // totals for a chosen period
  const [month, setMonth] = useState(now.month);
  const [year, setYear] = useState(now.year);
  const [monthlyTotal, setMonthlyTotal] = useState(0);
  const [yearlyTotal, setYearlyTotal] = useState(0);

  // sales table
  const [rows, setRows] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [form, setForm] = useState({ dealId: '', amount: '', invoiceNumber: '', saleDate: '' });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const loadTotals = useCallback(() => {
    salesApi.monthlySummary(month, year)
      .then(({ data }) => setMonthlyTotal(toNumber(data)))
      .catch(() => setMonthlyTotal(0));
    salesApi.yearlySummary(year)
      .then(({ data }) => setYearlyTotal(toNumber(data)))
      .catch(() => setYearlyTotal(0));
  }, [month, year]);

  const loadSales = useCallback(() => {
    setLoading(true);
    salesApi.listSales({ page, size })
      .then(({ data }) => {
        setRows(data.content || []);
        setTotalPages(data.totalPages || 0);
      })
      .catch((err) => setError(getErrorMessage(err, 'Failed to load sales.')))
      .finally(() => setLoading(false));
  }, [page, size]);

  useEffect(() => { loadTotals(); }, [loadTotals]);
  useEffect(() => { loadSales(); }, [loadSales]);

  const handleRecordSale = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setSaving(true);
    try {
      await salesApi.recordSale({
        dealId: Number(form.dealId),
        amount: Number(form.amount),
        invoiceNumber: form.invoiceNumber || null,
        saleDate: form.saleDate || null,
      });
      setForm({ dealId: '', amount: '', invoiceNumber: '', saleDate: '' });
      setSuccess('Sale recorded.');
      loadTotals();
      loadSales();
    } catch (err) {
      setError(getErrorMessage(err, 'Could not record sale.'));
    } finally {
      setSaving(false);
    }
  };

  const handlePaymentChange = async (id, status) => {
    setError('');
    setSuccess('');
    try {
      await salesApi.updatePaymentStatus(id, status);
      setRows((prev) => prev.map((r) => (r.id === id ? { ...r, paymentStatus: status } : r)));
      setSuccess('Payment status updated.');
    } catch (err) {
      setError(getErrorMessage(err, 'Could not update payment status.'));
      loadSales(); // put the dropdown back to the saved value
    }
  };

  const columns = [
    { key: 'id', header: 'Sale #' },
    { key: 'leadName', header: 'Lead / Deal', render: (r) => `${r.leadName} (Deal ${r.dealId})` },
    { key: 'assignedToName', header: 'Sold By', render: (r) => r.assignedToName || '-' },
    { key: 'saleAmount', header: 'Amount', render: (r) => formatINR(r.saleAmount) },
    { key: 'invoiceNumber', header: 'Invoice', render: (r) => r.invoiceNumber || '-' },
    { key: 'saleDate', header: 'Date', render: (r) => formatDate(r.saleDate) },
    {
      key: 'paymentStatus',
      header: 'Payment',
      render: (r) => can('SALE_CREATE') ? (
        <Select
          options={PAYMENT_STATUSES}
          value={r.paymentStatus}
          onChange={(e) => handlePaymentChange(r.id, e.target.value)}
        />
      ) : (
        <span className={`badge badge-${String(r.paymentStatus || '').toLowerCase()}`}>{r.paymentStatus}</span>
      ),
    },
  ];

  return (
    <div>
      <h1 className="page-title">Sales</h1>

      <div className="filter-bar">
        <Select label="Month" options={MONTHS} value={month} onChange={(e) => setMonth(Number(e.target.value))} />
        <Input label="Year" type="number" value={year}
               onChange={(e) => setYear(Number(e.target.value) || now.year)} />
      </div>

      <div className="stat-grid" style={{ marginBottom: '1.5rem' }}>
        <div className="stat-card">
          <span className="stat-label">{monthName(month)} {year} Sales Total</span>
          <span className="stat-value">{formatINR(monthlyTotal)}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">{year} Yearly Sales Total</span>
          <span className="stat-value">{formatINR(yearlyTotal)}</span>
        </div>
      </div>

      {can('SALE_CREATE') && (
        <div className="card">
          <h2 className="card-title">Record a Sale</h2>
          <form onSubmit={handleRecordSale} className="form-grid">
            <Input label="Deal ID" type="number" value={form.dealId}
                   onChange={(e) => setForm({ ...form, dealId: e.target.value })} />
            <Input label="Sale Amount" type="number" value={form.amount}
                   onChange={(e) => setForm({ ...form, amount: e.target.value })} />
            <Input label="Invoice Number" value={form.invoiceNumber}
                   onChange={(e) => setForm({ ...form, invoiceNumber: e.target.value })} />
            <Input label="Sale Date" type="date" value={form.saleDate}
                   onChange={(e) => setForm({ ...form, saleDate: e.target.value })} />
            <div className="form-actions">
              <Button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Record Sale'}</Button>
            </div>
          </form>
        </div>
      )}

      {error && <p className="form-error">{error}</p>}
      {success && <p className="muted">{success}</p>}

      <h2 className="card-title" style={{ marginTop: '1.5rem' }}>All Sales</h2>
      <DataTable columns={columns} rows={rows} loading={loading} />
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
    </div>
  );
}
