import { useEffect, useState } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import * as dealsApi from '../../api/deals.api';
import * as leadsApi from '../../api/leads.api';
import * as usersApi from '../../api/users.api';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import Button from '../../components/common/Button';
import { useAuth } from '../../hooks/useAuth';
import { isManagerOrAbove } from '../../utils/roles.util';
import { DEAL_STAGES } from '../../utils/constants';
import { getErrorMessage } from '../../utils/error.util';

export default function DealForm() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuth();
  const canAssign = isManagerOrAbove(user);
  const editingDeal = location.state?.deal;
  const preselectedLeadId = location.state?.leadId;

  const [form, setForm] = useState({
    leadId: preselectedLeadId || '',
    dealStage: editingDeal?.dealStage || 'NEGOTIATION',
    expectedAmount: editingDeal?.expectedAmount ?? '',
    closingDate: editingDeal?.closingDate || '',
    assignedTo: editingDeal?.assignedTo || '',
  });
  const [leads, setLeads] = useState([]);
  const [users, setUsers] = useState([]);
  const [saving, setSaving] = useState(false);
  const [apiError, setApiError] = useState('');

  useEffect(() => {
    (async () => {
      try {
        const { data } = await leadsApi.listLeads({ size: 100 });
        // every lead that is not lost can be converted (backend marks it QUALIFIED)
        let options = (data.content || []).filter((l) => l.status !== 'LOST');
        // make sure the lead we came from is selectable even if it isn't QUALIFIED
        if (preselectedLeadId && !options.some((l) => l.id === preselectedLeadId)) {
          try {
            const { data: lead } = await leadsApi.getLead(preselectedLeadId);
            options = [lead, ...options];
          } catch { /* ignore */ }
        }
        setLeads(options);
      } catch (err) {
        setApiError(getErrorMessage(err, 'Failed to load leads.'));
      }
      // needs USER_READ; without it the assignee list stays empty
      if (canAssign) {
        usersApi.listUsers({ size: 100 }).then(({ data }) => setUsers(data.content || [])).catch(() => setUsers([]));
      }
    })();
  }, [preselectedLeadId, canAssign]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiError('');
    setSaving(true);
    try {
      const payload = {
        ...form,
        leadId: form.leadId || null,
        assignedTo: form.assignedTo || null,
        expectedAmount: form.expectedAmount ? Number(form.expectedAmount) : 0,
      };
      if (isEdit) {
        await dealsApi.updateDeal(id, payload);
      } else {
        await dealsApi.convertLeadToDeal(payload);
      }
      navigate('/deals');
    } catch (err) {
      setApiError(getErrorMessage(err, 'Could not save deal.'));
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="card">
      <h1 className="page-title">{isEdit ? 'Edit Deal' : 'New Deal'}</h1>
      <form onSubmit={handleSubmit} className="form-grid">
        {!isEdit && (
          <Select
            label="Lead"
            placeholder="Select a lead"
            options={leads.map((l) => ({ value: l.id, label: l.name }))}
            value={form.leadId}
            onChange={(e) => setForm({ ...form, leadId: e.target.value })}
          />
        )}
        <Select label="Stage" options={DEAL_STAGES.filter((s) => !String(s.value ?? s).startsWith('CLOSED') || (editingDeal && editingDeal.dealStage === (s.value ?? s)))} value={form.dealStage}
                onChange={(e) => setForm({ ...form, dealStage: e.target.value })} />
        <Input label="Expected Amount" type="number" value={form.expectedAmount}
               onChange={(e) => setForm({ ...form, expectedAmount: e.target.value })} />
        <Input label="Closing Date" type="date" value={form.closingDate}
               onChange={(e) => setForm({ ...form, closingDate: e.target.value })} />
        {canAssign ? (
          <Select
            label="Assigned To"
            placeholder="Unassigned"
            options={users.map((u) => ({ value: u.id, label: u.name }))}
            value={form.assignedTo}
            onChange={(e) => setForm({ ...form, assignedTo: e.target.value })}
          />
        ) : (
          <p className="muted">This deal stays assigned to you.</p>
        )}

        {apiError && <p className="form-error">{apiError}</p>}

        <div className="form-actions">
          <Button type="button" variant="secondary" onClick={() => navigate('/deals')}>Cancel</Button>
          <Button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</Button>
        </div>
      </form>
    </div>
  );
}
