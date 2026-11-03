import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as targetsApi from '../../api/targets.api';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';

export default function TargetForm() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ userId: '', month: '', year: new Date().getFullYear(), amount: '' });
  const [saving, setSaving] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await targetsApi.assignTarget({
        userId: Number(form.userId),
        month: Number(form.month),
        year: Number(form.year),
        amount: Number(form.amount),
      });
      navigate('/targets');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="card">
      <h1 className="page-title">Assign Target</h1>
      <form onSubmit={handleSubmit} className="form-grid">
        <Input label="User ID" value={form.userId} onChange={(e) => setForm({ ...form, userId: e.target.value })} />
        <Input label="Month (1-12)" type="number" min="1" max="12" value={form.month}
               onChange={(e) => setForm({ ...form, month: e.target.value })} />
        <Input label="Year" type="number" value={form.year}
               onChange={(e) => setForm({ ...form, year: e.target.value })} />
        <Input label="Target Amount" type="number" value={form.amount}
               onChange={(e) => setForm({ ...form, amount: e.target.value })} />
        <div className="form-actions">
          <Button type="button" variant="secondary" onClick={() => navigate('/targets')}>Cancel</Button>
          <Button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</Button>
        </div>
      </form>
    </div>
  );
}
