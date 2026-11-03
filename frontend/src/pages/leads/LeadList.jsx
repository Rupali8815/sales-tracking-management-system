import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import * as leadsApi from '../../api/leads.api';
import DataTable from '../../components/tables/DataTable';
import Pagination from '../../components/tables/Pagination';
import TableActions from '../../components/tables/TableActions';
import Select from '../../components/common/Select';
import Button from '../../components/common/Button';
import { usePermission } from '../../hooks/usePermission';
import { useAuth } from '../../hooks/useAuth';
import { isOwnerScoped } from '../../utils/roles.util';
import { usePagination } from '../../hooks/usePagination';
import { LEAD_STATUSES } from '../../utils/constants';
import { formatDate } from '../../utils/date.util';
import { getErrorMessage } from '../../utils/error.util';

export default function LeadList() {
  const navigate = useNavigate();
  const { can } = usePermission();
  const { user } = useAuth();
  const { page, size, setPage } = usePagination();

  const [status, setStatus] = useState('');
  const [rows, setRows] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchLeads = useCallback(() => {
    setLoading(true);
    leadsApi.listLeads({ status: status || undefined, page, size })
      .then(({ data }) => {
        setRows(data.content || []);
        setTotalPages(data.totalPages || 0);
      })
      .catch((err) => setError(getErrorMessage(err, 'Failed to load leads.')))
      .finally(() => setLoading(false));
  }, [status, page, size]);

  useEffect(() => { fetchLeads(); }, [fetchLeads]);

  const columns = [
    { key: 'name', header: 'Name', render: (row) => (
        <button className="link-btn" onClick={() => navigate(`/leads/${row.id}`)}>{row.name}</button>
      ) },
    { key: 'email', header: 'Email' },
    { key: 'phone', header: 'Phone' },
    { key: 'source', header: 'Source' },
    { key: 'status', header: 'Status', render: (row) => <span className={`badge badge-${String(row.status || '').toLowerCase()}`}>{row.status}</span> },
    { key: 'assignedToName', header: 'Assigned To' },
    { key: 'createdAt', header: 'Created', render: (row) => formatDate(row.createdAt) },
    {
      key: 'actions',
      header: '',
      render: (row) => (
        <TableActions
          canEdit={can('LEAD_UPDATE') && row.editable !== false}
          canDelete={false}
          onEdit={() => navigate(`/leads/${row.id}/edit`)}
        />
      ),
    },
  ];

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{isOwnerScoped(user) ? 'My Leads' : 'Leads'}</h1>
        {can('LEAD_CREATE') && <Button onClick={() => navigate('/leads/new')}>+ New Lead</Button>}
      </div>

      <div className="filter-bar">
        <Select
          placeholder="All statuses"
          options={LEAD_STATUSES}
          value={status}
          onChange={(e) => setStatus(e.target.value)}
        />
      </div>

      {error && <p className="form-error">{error}</p>}
      <DataTable columns={columns} rows={rows} loading={loading} />
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
    </div>
  );
}
