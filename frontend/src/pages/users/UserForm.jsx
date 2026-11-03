import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import * as usersApi from '../../api/users.api';
import * as rolesApi from '../../api/roles.api';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Button from '../../components/common/Button';
import { USER_STATUSES } from '../../utils/constants';
import { isRequired, isEmail } from '../../utils/validation.util';
import { getErrorMessage } from '../../utils/error.util';

const emptyForm = { name: '', email: '', password: '', phone: '', status: 'ACTIVE', roleIds: [] };

export default function UserForm() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [form, setForm] = useState(emptyForm);
  const [roles, setRoles] = useState([]);
  const [errors, setErrors] = useState({});
  const [saving, setSaving] = useState(false);
  const [apiError, setApiError] = useState('');

  useEffect(() => {
    (async () => {
      try {
        const { data: allRoles } = await rolesApi.listRoles();
        setRoles(allRoles);
        if (isEdit) {
          const { data } = await usersApi.getUser(id);
          // UserResponse returns role NAMES; map them back to role IDs so saving doesn't wipe roles
          const names = new Set(data.roles || []);
          setForm({
            name: data.name,
            email: data.email,
            password: '',
            phone: data.phone || '',
            status: data.status,
            roleIds: allRoles.filter((r) => names.has(r.roleName)).map((r) => r.id),
          });
        }
      } catch (err) {
        setApiError(getErrorMessage(err, 'Failed to load data.'));
      }
    })();
  }, [id, isEdit]);

  const validate = () => {
    const next = {};
    if (!isRequired(form.name)) next.name = 'Name is required';
    if (!isRequired(form.email) || !isEmail(form.email)) next.email = 'Valid email is required';
    if (!isEdit && !isRequired(form.password)) next.password = 'Password is required';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setApiError('');
    setSaving(true);
    try {
      if (isEdit) {
        await usersApi.updateUser(id, form);
      } else {
        await usersApi.createUser(form);
      }
      navigate('/users');
    } catch (err) {
      setApiError(getErrorMessage(err, 'Could not save user.'));
    } finally {
      setSaving(false);
    }
  };

  const toggleRole = (roleId) => {
    setForm((prev) => ({
      ...prev,
      roleIds: prev.roleIds.includes(roleId)
        ? prev.roleIds.filter((r) => r !== roleId)
        : [...prev.roleIds, roleId],
    }));
  };

  return (
    <div className="card">
      <h1 className="page-title">{isEdit ? 'Edit User' : 'New User'}</h1>
      <form onSubmit={handleSubmit} className="form-grid">
        <Input label="Name" value={form.name} error={errors.name}
               onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <Input label="Email" type="email" value={form.email} error={errors.email}
               onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <Input label={isEdit ? 'New Password (optional)' : 'Password'} type="password"
               value={form.password} error={errors.password}
               onChange={(e) => setForm({ ...form, password: e.target.value })} />
        <Input label="Phone" value={form.phone}
               onChange={(e) => setForm({ ...form, phone: e.target.value })} />
        <Select label="Status" options={USER_STATUSES} value={form.status}
                onChange={(e) => setForm({ ...form, status: e.target.value })} />

        <div className="form-field">
          <label className="form-label">Roles</label>
          <div className="checkbox-group">
            {roles.map((role) => (
              <label key={role.id} className="checkbox-item">
                <input
                  type="checkbox"
                  checked={form.roleIds.includes(role.id)}
                  onChange={() => toggleRole(role.id)}
                />
                {role.roleName}
              </label>
            ))}
          </div>
        </div>

        {apiError && <p className="form-error">{apiError}</p>}

        <div className="form-actions">
          <Button type="button" variant="secondary" onClick={() => navigate('/users')}>Cancel</Button>
          <Button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</Button>
        </div>
      </form>
    </div>
  );
}
