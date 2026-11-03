import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import * as usersApi from '../../api/users.api';
import DataTable from '../../components/tables/DataTable';
import Pagination from '../../components/tables/Pagination';
import TableActions from '../../components/tables/TableActions';
import ConfirmDialog from '../../components/common/ConfirmDialog';
import Button from '../../components/common/Button';
import { usePermission } from '../../hooks/usePermission';
import { usePagination } from '../../hooks/usePagination';
import { getErrorMessage } from '../../utils/error.util';

export default function UserList() {
  const navigate = useNavigate();
  const { can } = usePermission();
  const { page, size, setPage } = usePagination();

  const [rows, setRows] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [pendingDeleteId, setPendingDeleteId] = useState(null);

  const fetchUsers = useCallback(() => {
    setLoading(true);
    usersApi.listUsers({ page, size })
      .then(({ data }) => {
        setRows(data.content || []);
        setTotalPages(data.totalPages || 0);
      })
      .catch((err) => setError(getErrorMessage(err, 'Failed to load users.')))
      .finally(() => setLoading(false));
  }, [page, size]);

  useEffect(() => { fetchUsers(); }, [fetchUsers]);

  const handleDeactivate = async () => {
    setError('');
    try {
      await usersApi.deactivateUser(pendingDeleteId);
      fetchUsers();
    } catch (err) {
      setError(getErrorMessage(err, 'Could not deactivate user.'));
    } finally {
      setPendingDeleteId(null);
    }
  };

  const columns = [
    { key: 'name', header: 'Name' },
    { key: 'email', header: 'Email' },
    { key: 'phone', header: 'Phone' },
    { key: 'status', header: 'Status' },
    { key: 'roles', header: 'Roles', render: (row) => (row.roles || []).join(', ') },
    {
      key: 'actions',
      header: '',
      render: (row) => (
        <TableActions
          canEdit={can('USER_UPDATE')}
          canDelete={can('USER_DELETE')}
          onEdit={() => navigate(`/users/${row.id}/edit`)}
          onDelete={() => setPendingDeleteId(row.id)}
        />
      ),
    },
  ];

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Users</h1>
        {can('USER_CREATE') && (
          <Button onClick={() => navigate('/users/new')}>+ New User</Button>
        )}
      </div>

      {error && <p className="form-error">{error}</p>}
      <DataTable columns={columns} rows={rows} loading={loading} />
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />

      <ConfirmDialog
        open={pendingDeleteId !== null}
        message="Deactivate this user? They will no longer be able to sign in."
        onConfirm={handleDeactivate}
        onCancel={() => setPendingDeleteId(null)}
      />
    </div>
  );
}
