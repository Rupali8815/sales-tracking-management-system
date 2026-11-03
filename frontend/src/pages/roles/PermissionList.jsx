import { useEffect, useState } from 'react';
import * as permissionsApi from '../../api/permissions.api';
import DataTable from '../../components/tables/DataTable';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import { isRequired } from '../../utils/validation.util';

export default function PermissionList() {
  const [permissions, setPermissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [code, setCode] = useState('');
  const [description, setDescription] = useState('');
  const [error, setError] = useState('');

  const load = () =>
    permissionsApi.listPermissions()
      .then(({ data }) => setPermissions(Array.isArray(data) ? data : []))
      .catch(() => setPermissions([]))
      .finally(() => setLoading(false));

  useEffect(() => { load(); }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    setError('');
    if (!isRequired(code)) {
      setError('Permission code is required.');
      return;
    }
    try {
      await permissionsApi.createPermission(code.trim().toUpperCase(), description);
      setCode('');
      setDescription('');
      load();
    } catch (err) {
      // e.g. 400 duplicate code
      setError(err?.response?.data?.message || 'Failed to create permission.');
    }
  };

  const columns = [
    { key: 'permissionCode', header: 'Code' },
    { key: 'description', header: 'Description' },
  ];

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Permissions</h1>
      </div>

      <form onSubmit={handleAdd} className="card form-grid">
        <Input label="Permission Code" value={code} onChange={(e) => setCode(e.target.value)} />
        <Input label="Description" value={description} onChange={(e) => setDescription(e.target.value)} />
        {error && <p className="form-error">{error}</p>}
        <div className="form-actions">
          <Button type="submit">+ Add Permission</Button>
        </div>
      </form>

      <DataTable columns={columns} rows={permissions} loading={loading} />
    </div>
  );
}
