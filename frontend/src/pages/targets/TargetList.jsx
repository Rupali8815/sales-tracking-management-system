import { useState } from 'react';
import * as targetsApi from '../../api/targets.api';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import DataTable from '../../components/tables/DataTable';
import { monthName } from '../../utils/date.util';
import { getErrorMessage, formatINR } from '../../utils/error.util';

export default function TargetList() {
  const [userId, setUserId] = useState('');
  const [year, setYear] = useState(new Date().getFullYear());
  const [targets, setTargets] = useState([]);

  const [assignForm, setAssignForm] = useState({ userId: '', month: '', year: new Date().getFullYear(), amount: '' });
  const [achievement, setAchievement] = useState(null);
  const [error, setError] = useState('');
  const [info, setInfo] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const { data } = await targetsApi.listTargets(userId, year);
      setTargets(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(getErrorMessage(err, 'Could not load targets.'));
    }
  };

  const handleAssign = async (e) => {
    e.preventDefault();
    setError('');
    setInfo('');
    try {
      await targetsApi.assignTarget({
        userId: Number(assignForm.userId),
        month: Number(assignForm.month),
        year: Number(assignForm.year),
        amount: Number(assignForm.amount),
      });
      setInfo('Target assigned.');
      if (assignForm.userId === userId) handleSearch(e);
    } catch (err) {
      setError(getErrorMessage(err, 'Could not assign target.'));
    }
  };

  const handleCheckAchievement = async () => {
    if (!userId) return;
    const currentMonth = new Date().getMonth() + 1;
    setError('');
    try {
      const { data } = await targetsApi.getAchievement(userId, currentMonth, year);
      setAchievement(data === '' ? 0 : data);
    } catch (err) {
      setError(getErrorMessage(err, 'Could not load achievement.'));
    }
  };

  const columns = [
    { key: 'targetMonth', header: 'Month', render: (row) => monthName(row.targetMonth) },
    { key: 'targetYear', header: 'Year' },
    { key: 'targetAmount', header: 'Target Amount', render: (row) => formatINR(row.targetAmount) },
  ];

  return (
    <div>
      <h1 className="page-title">Targets</h1>
      {error && <p className="form-error">{error}</p>}
      {info && <p className="muted">{info}</p>}

      <div className="card">
        <h2 className="card-title">Assign Target</h2>
        <form onSubmit={handleAssign} className="form-grid">
          <Input label="User ID" value={assignForm.userId} onChange={(e) => setAssignForm({ ...assignForm, userId: e.target.value })} />
          <Input label="Month (1-12)" type="number" min="1" max="12" value={assignForm.month}
                 onChange={(e) => setAssignForm({ ...assignForm, month: e.target.value })} />
          <Input label="Year" type="number" value={assignForm.year}
                 onChange={(e) => setAssignForm({ ...assignForm, year: e.target.value })} />
          <Input label="Target Amount" type="number" value={assignForm.amount}
                 onChange={(e) => setAssignForm({ ...assignForm, amount: e.target.value })} />
          <div className="form-actions">
            <Button type="submit">Assign</Button>
          </div>
        </form>
      </div>

      <div className="card">
        <h2 className="card-title">View Targets</h2>
        <form onSubmit={handleSearch} className="filter-bar">
          <Input placeholder="User ID" value={userId} onChange={(e) => setUserId(e.target.value)} />
          <Input placeholder="Year" type="number" value={year} onChange={(e) => setYear(e.target.value)} />
          <Button type="submit">Search</Button>
          <Button type="button" variant="secondary" onClick={handleCheckAchievement}>Check This Month's Achievement</Button>
        </form>

        {achievement !== null && <p className="stat-value">{achievement}% achieved this month</p>}

        <DataTable columns={columns} rows={targets} loading={false} />
      </div>
    </div>
  );
}
