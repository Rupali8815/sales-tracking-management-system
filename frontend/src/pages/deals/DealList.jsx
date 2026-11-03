import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import * as dealsApi from '../../api/deals.api';
import DataTable from '../../components/tables/DataTable';
import Pagination from '../../components/tables/Pagination';
import Select from '../../components/common/Select';
import Button from '../../components/common/Button';
import { usePermission } from '../../hooks/usePermission';
import { useAuth } from '../../hooks/useAuth';
import { isOwnerScoped } from '../../utils/roles.util';
import { usePagination } from '../../hooks/usePagination';
import { DEAL_STAGES } from '../../utils/constants';
import { formatDate } from '../../utils/date.util';
import { getErrorMessage, formatINR } from '../../utils/error.util';

export default function DealList() {
  const navigate = useNavigate();
  const { can } = usePermission();
  const { user } = useAuth();
  const { page, size, setPage } = usePagination();

  const [stage, setStage] = useState('');
  const [rows, setRows] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchDeals = useCallback(() => {
    setLoading(true);
    dealsApi.listDeals({ stage: stage || undefined, page, size })
      .then(({ data }) => {
        setRows(data.content || []);
        setTotalPages(data.totalPages || 0);
      })
      .catch((err) => setError(getErrorMessage(err, 'Failed to load deals.')))
      .finally(() => setLoading(false));
  }, [stage, page, size]);

  useEffect(() => { fetchDeals(); }, [fetchDeals]);

  const handleClose = async (id, outcome) => {
    setError('');
    try {
      await dealsApi.closeDeal(id, outcome);
      fetchDeals();
    } catch (err) {
      setError(getErrorMessage(err, 'Could not update deal.'));
    }
  };

  const columns = [
    { key: 'leadName', header: 'Lead' },
    { key: 'dealStage', header: 'Stage', render: (row) => <span className={`badge badge-${String(row.dealStage || '').toLowerCase()}`}>{row.dealStage}</span> },
    { key: 'expectedAmount', header: 'Expected Amount', render: (row) => formatINR(row.expectedAmount) },
    { key: 'closingDate', header: 'Closing Date', render: (row) => formatDate(row.closingDate) },
    { key: 'assignedToName', header: 'Assigned To' },
    {
      key: 'actions',
      header: '',
      render: (row) => (
        <div className="table-actions">
          {can('DEAL_UPDATE') && row.editable && (
            <button className="link-btn" onClick={() => navigate(`/deals/${row.id}/edit`, { state: { deal: row } })}>Edit</button>
          )}
          {(row.dealStage === 'CLOSED_WON' || row.dealStage === 'CLOSED_LOST') && !row.editable && (
            <span className="muted">Locked</span>
          )}
          {can('DEAL_UPDATE') && row.editable && row.dealStage !== 'CLOSED_WON' && row.dealStage !== 'CLOSED_LOST' && (
            <>
              <button className="link-btn" onClick={() => handleClose(row.id, 'CLOSED_WON')}>Mark Won</button>
              <button className="link-btn link-btn-danger" onClick={() => handleClose(row.id, 'CLOSED_LOST')}>Mark Lost</button>
            </>
          )}
        </div>
      ),
    },
  ];

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{isOwnerScoped(user) ? 'My Deals' : 'Deals'}</h1>
        {can('DEAL_CREATE') && <Button onClick={() => navigate('/deals/new')}>+ New Deal</Button>}
      </div>

      <div className="filter-bar">
        <Select placeholder="All stages" options={DEAL_STAGES} value={stage} onChange={(e) => setStage(e.target.value)} />
      </div>

      {error && <p className="form-error">{error}</p>}
      <DataTable columns={columns} rows={rows} loading={loading} />
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
    </div>
  );
}
