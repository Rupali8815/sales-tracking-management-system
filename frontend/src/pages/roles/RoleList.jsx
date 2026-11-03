import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as rolesApi from '../../api/roles.api';
import DataTable from '../../components/tables/DataTable';
import Button from '../../components/common/Button';
import { usePermission } from '../../hooks/usePermission';

export default function RoleList() {
  const navigate = useNavigate();
  const { can } = usePermission();
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    rolesApi.listRoles()
      .then(({ data }) => setRoles(Array.isArray(data) ? data : []))
      .catch(() => setRoles([]))
      .finally(() => setLoading(false));
  }, []);

  const columns = [
    { key: 'roleName', header: 'Role Name' },
    { key: 'description', header: 'Description' },
    {
      key: 'permissions',
      header: 'Permissions',
      render: (row) => (row.permissions || []).join(', ') || '-',
    },
    {
      key: 'actions',
      header: '',
      render: (row) => (
        can('ROLE_MANAGE') && (
          <button className="link-btn" onClick={() => navigate(`/roles/${row.id}/edit`)}>
            Manage Permissions
          </button>
        )
      ),
    },
  ];

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Roles</h1>
        {can('ROLE_MANAGE') && <Button onClick={() => navigate('/roles/new')}>+ New Role</Button>}
      </div>
      <DataTable columns={columns} rows={roles} loading={loading} />
    </div>
  );
}
