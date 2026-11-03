import { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import * as leadsApi from '../../api/leads.api';
import Loader from '../../components/common/Loader';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import { usePermission } from '../../hooks/usePermission';
import { formatDate } from '../../utils/date.util';
import { getErrorMessage } from '../../utils/error.util';

export default function LeadDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { can } = usePermission();

  const [lead, setLead] = useState(null);
  const [note, setNote] = useState('');
  const [activities, setActivities] = useState([]);
  const [error, setError] = useState('');

  const fetchLead = useCallback(() => {
    leadsApi.getLead(id)
      .then(({ data }) => setLead(data))
      .catch((err) => setError(getErrorMessage(err, 'Failed to load lead.')));
  }, [id]);

  const fetchActivities = useCallback(() => {
    leadsApi.getLeadActivities(id)
      .then(({ data }) => setActivities(Array.isArray(data) ? data : []))
      .catch(() => setActivities([]));
  }, [id]);

  useEffect(() => { fetchLead(); fetchActivities(); }, [fetchLead, fetchActivities]);

  const handleAddNote = async () => {
    if (!note.trim()) return;
    setError('');
    try {
      await leadsApi.addLeadActivity(id, 'NOTE', note);
      setNote('');
      fetchActivities();
    } catch (err) {
      setError(getErrorMessage(err, 'Could not add note.'));
    }
  };

  if (!lead) return error ? <p className="form-error">{error}</p> : <Loader label="Loading lead..." />;

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">{lead.name}</h1>
        <div>
          {can('LEAD_UPDATE') && lead.editable && (
            <Button variant="secondary" onClick={() => navigate(`/leads/${id}/edit`)}>Edit</Button>
          )}
          {can('DEAL_CREATE') && lead.editable && lead.status !== 'LOST' && (
            <Button onClick={() => navigate('/deals/new', { state: { leadId: lead.id } })}>
              Convert to Deal
            </Button>
          )}
        </div>
      </div>

      <div className="card">
        <dl className="detail-list">
          <dt>Email</dt><dd>{lead.email || '-'}</dd>
          <dt>Phone</dt><dd>{lead.phone || '-'}</dd>
          <dt>Source</dt><dd>{lead.source || '-'}</dd>
          <dt>Status</dt><dd><span className={`badge badge-${String(lead.status || '').toLowerCase()}`}>{lead.status}</span></dd>
          <dt>Assigned To</dt><dd>{lead.assignedToName || 'Unassigned'}</dd>
          <dt>Created</dt><dd>{formatDate(lead.createdAt)}</dd>
        </dl>
      </div>

      <div className="card">
        <h2 className="card-title">Activity</h2>
        {lead.editable && (
          <div className="note-composer">
            <Input placeholder="Add a note about this lead..." value={note} onChange={(e) => setNote(e.target.value)} />
            <Button onClick={handleAddNote}>Add Note</Button>
          </div>
        )}
        {error && <p className="form-error">{error}</p>}
        {activities.length === 0 ? (
          <p className="muted">No activity yet. Add a note after you speak to this lead.</p>
        ) : (
          <ul className="timeline">
            {activities.map((a) => (
              <li key={a.id}>
                <div className="timeline-meta">
                  {a.activityType} &middot; {a.createdByName || 'Unknown'} &middot; {a.createdAt ? new Date(a.createdAt).toLocaleString('en-IN') : ''}
                </div>
                {a.notes && <p className="timeline-note">{a.notes}</p>}
              </li>
            ))}
          </ul>
        )}
      </div>

      <Link to="/leads" className="link-btn">&larr; Back to Leads</Link>
    </div>
  );
}
