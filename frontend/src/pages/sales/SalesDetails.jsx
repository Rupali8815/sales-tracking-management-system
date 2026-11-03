import { useState } from 'react';
import * as salesApi from '../../api/sales.api';
import Select from '../../components/common/Select';
import Button from '../../components/common/Button';
import { PAYMENT_STATUSES } from '../../utils/constants';

/**
 * Lightweight utility view for updating an individual sale's payment status,
 * since sale records are primarily managed via the Sales list/record form.
 */
export default function SalesDetails() {
  const [saleId, setSaleId] = useState('');
  const [status, setStatus] = useState('PAID');
  const [message, setMessage] = useState('');

  const handleUpdate = async (e) => {
    e.preventDefault();
    await salesApi.updatePaymentStatus(Number(saleId), status);
    setMessage(`Sale #${saleId} marked as ${status}.`);
  };

  return (
    <div className="card">
      <h1 className="page-title">Update Payment Status</h1>
      <form onSubmit={handleUpdate} className="form-grid">
        <label className="form-field">
          <span className="form-label">Sale ID</span>
          <input className="form-input" value={saleId} onChange={(e) => setSaleId(e.target.value)} />
        </label>
        <Select label="Payment Status" options={PAYMENT_STATUSES} value={status}
                onChange={(e) => setStatus(e.target.value)} />
        <div className="form-actions">
          <Button type="submit">Update</Button>
        </div>
      </form>
      {message && <p className="muted">{message}</p>}
    </div>
  );
}
