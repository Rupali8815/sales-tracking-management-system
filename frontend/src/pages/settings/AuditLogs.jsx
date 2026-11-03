import { useEffect, useState } from 'react';
import axiosClient from '../../api/axiosClient';
import DataTable from '../../components/tables/DataTable';
import Pagination from '../../components/tables/Pagination';
import { usePagination } from '../../hooks/usePagination';
import { getErrorMessage } from '../../utils/error.util';

export default function AuditLogs() {
  const { page, size, setPage } = usePagination();
  const [rows, setRows] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    axiosClient.get('/audit-logs', { params: { page, size } })
      .then(({ data }) => {
        setRows(data.content || []);
        setTotalPages(data.totalPages || 0);
      })
      .catch((err) => setError(getErrorMessage(err, 'Failed to load audit logs.')))
      .finally(() => setLoading(false));
  }, [page, size]);

  const columns = [
    { key: 'userEmail', header: 'User', render: (row) => row.userEmail || '-' },
    { key: 'action', header: 'Action', render: (row) => <span className={`badge badge-${String(row.action || '').toLowerCase()}`}>{row.action}</span> },
    { key: 'entityName', header: 'Entity' },
    { key: 'entityId', header: 'Entity ID' },
    { key: 'timestamp', header: 'Timestamp', render: (row) => row.timestamp ? new Date(row.timestamp).toLocaleString('en-IN') : '-' },
  ];

  return (
    <div>
      <h1 className="page-title">Audit Logs</h1>
      {error && <p className="form-error">{error}</p>}
      <DataTable columns={columns} rows={rows} loading={loading} emptyMessage="No activity recorded yet. Actions like creating a lead or recording a sale will appear here." />
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
    </div>
  );
}
