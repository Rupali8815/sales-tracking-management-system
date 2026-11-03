import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import * as leadsApi from '../../api/leads.api';
import * as usersApi from '../../api/users.api';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Button from '../../components/common/Button';
import { LEAD_STATUSES } from '../../utils/constants';
import { useAuth } from '../../hooks/useAuth';
import { isManagerOrAbove } from '../../utils/roles.util';
import { isRequired } from '../../utils/validation.util';
import { getErrorMessage } from '../../utils/error.util';

const emptyForm = { name: '', email: '', phone: '', source: '', status: 'NEW', assignedTo: '' };

export default function LeadForm() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const { user } = useAuth();
  const canAssign = isManagerOrAbove(user);

  const [form, setForm] = useState(emptyForm);
  const [users, setUsers] = useState([]);
  const [errors, setErrors] = useState({});
  const [saving, setSaving] = useState(false);
  const [apiError, setApiError] = useState('');

  useEffect(() => {
    // needs USER_READ; users without it just can't pick an assignee
    if (canAssign) {
      usersApi.listUsers({ size: 100 }).then(({ data }) => setUsers(data.content || [])).catch(() => setUsers([]));
    }
    if (isEdit) {
      leadsApi.getLead(id).then(({ data }) => {
        setForm({
          name: data.name,
          email: data.email || '',
          phone: data.phone || '',
          source: data.source || '',
          status: data.status,
          assignedTo: data.assignedTo || '',
        });
      }).catch((err) => setApiError(getErrorMessage(err, 'Failed to load lead.')));
    }
  }, [id, isEdit, canAssign]);

  const validate = () => {
    const next = {};
    if (!isRequired(form.name)) next.name = 'Name is required';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setApiError('');
    setSaving(true);
    try {
      const payload = { ...form, assignedTo: form.assignedTo || null };
      if (isEdit) {
        await leadsApi.updateLead(id, payload);
      } else {
        await leadsApi.createLead(payload);
      }
      navigate('/leads');
    } catch (err) {
      setApiError(getErrorMessage(err, 'Could not save lead.'));
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="card">
      <h1 className="page-title">{isEdit ? 'Edit Lead' : 'New Lead'}</h1>
      <form onSubmit={handleSubmit} className="form-grid">
        <Input label="Name" value={form.name} error={errors.name}
               onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <Input label="Email" type="email" value={form.email}
               onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <Input label="Phone" value={form.phone}
               onChange={(e) => setForm({ ...form, phone: e.target.value })} />
        <Input label="Source" value={form.source} placeholder="e.g. Website, Referral, Cold Call"
               onChange={(e) => setForm({ ...form, source: e.target.value })} />
        <Select label="Status" options={LEAD_STATUSES} value={form.status}
                onChange={(e) => setForm({ ...form, status: e.target.value })} />
        {canAssign ? (
          <Select
            label="Assigned To"
            placeholder="Unassigned"
            options={users.map((u) => ({ value: u.id, label: u.name }))}
            value={form.assignedTo}
            onChange={(e) => setForm({ ...form, assignedTo: e.target.value })}
          />
        ) : (
          <p className="muted">This lead is assigned to you automatically.</p>
        )}

        {apiError && <p className="form-error">{apiError}</p>}

        <div className="form-actions">
          <Button type="button" variant="secondary" onClick={() => navigate('/leads')}>Cancel</Button>
          <Button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</Button>
        </div>
      </form>
    </div>
  );
}
