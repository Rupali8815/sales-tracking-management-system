import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import * as rolesApi from '../../api/roles.api';
import * as permissionsApi from '../../api/permissions.api';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import { isRequired } from '../../utils/validation.util';

const errMsg = (err, fallback) => err?.response?.data?.message || fallback;

export default function RoleForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEdit = Boolean(id);

  const [roleName, setRoleName] = useState('');
  const [description, setDescription] = useState('');
  const [permissions, setPermissions] = useState([]);
  const [selectedPermissionIds, setSelectedPermissionIds] = useState([]);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const { data: allPerms } = await permissionsApi.listPermissions();
        setPermissions(allPerms);

        if (isEdit) {
          // RoleResponse.permissions = Set<String> of codes -> map back to IDs
          const { data: roles } = await rolesApi.listRoles();
          const role = roles.find((r) => String(r.id) === String(id));
          if (!role) {
            setError('Role not found.');
            return;
          }
          setRoleName(role.roleName);
          setDescription(role.description || '');
          const codes = new Set(role.permissions || []);
          setSelectedPermissionIds(
            allPerms.filter((p) => codes.has(p.permissionCode)).map((p) => p.id)
          );
        }
      } catch (err) {
        setError(errMsg(err, 'Failed to load data.'));
      }
    })();
  }, [id, isEdit]);

  const togglePermission = (pid) => {
    setSelectedPermissionIds((prev) =>
      prev.includes(pid) ? prev.filter((p) => p !== pid) : [...prev, pid]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!isEdit && !isRequired(roleName)) {
      setError('Role name is required.');
      return;
    }

    setSaving(true);
    try {
      let roleId = id;
      if (!isEdit) {
        const { data: role } = await rolesApi.createRole(roleName, description);
        roleId = role.id;
      }
      if (isEdit || selectedPermissionIds.length > 0) {
        await rolesApi.assignPermissions(roleId, selectedPermissionIds);
      }
      navigate('/roles');
    } catch (err) {
      // e.g. 400 "A role with this name already exists: ..."
      setError(errMsg(err, 'Failed to save role.'));
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="card">
      <h1 className="page-title">{isEdit ? 'Manage Role Permissions' : 'New Role'}</h1>
      <form onSubmit={handleSubmit} className="form-grid">
        <Input
          label="Role Name"
          value={roleName}
          disabled={isEdit}
          onChange={(e) => setRoleName(e.target.value)}
        />
        <Input
          label="Description"
          value={description}
          disabled={isEdit}
          onChange={(e) => setDescription(e.target.value)}
        />

        <div className="form-field">
          <label className="form-label">Permissions</label>
          <div className="checkbox-group">
            {permissions.map((perm) => (
              <label key={perm.id} className="checkbox-item">
                <input
                  type="checkbox"
                  checked={selectedPermissionIds.includes(perm.id)}
                  onChange={() => togglePermission(perm.id)}
                />
                {perm.permissionCode}
              </label>
            ))}
          </div>
        </div>

        {error && <p className="form-error">{error}</p>}

        <div className="form-actions">
          <Button type="button" variant="secondary" onClick={() => navigate('/roles')}>Cancel</Button>
          <Button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</Button>
        </div>
      </form>
    </div>
  );
}
