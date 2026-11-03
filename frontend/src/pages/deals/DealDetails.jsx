import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import * as dealsApi from '../../api/deals.api';
import Loader from '../../components/common/Loader';
import { formatDate } from '../../utils/date.util';

export default function DealDetails() {
  const { id } = useParams();
  const [deal, setDeal] = useState(null);

  useEffect(() => {
    // listDeals is paginated; for a single deal, filtering client-side is a
    // reasonable placeholder until a dedicated GET /api/deals/{id} endpoint is added.
    dealsApi.listDeals({ size: 1000 }).then(({ data }) => {
      setDeal(data.content.find((d) => String(d.id) === id));
    });
  }, [id]);

  if (!deal) return <Loader label="Loading deal..." />;

  return (
    <div className="card">
      <h1 className="page-title">{deal.leadName}</h1>
      <dl className="detail-list">
        <dt>Stage</dt><dd>{deal.dealStage}</dd>
        <dt>Expected Amount</dt><dd>₹{Number(deal.expectedAmount).toLocaleString('en-IN')}</dd>
        <dt>Closing Date</dt><dd>{formatDate(deal.closingDate)}</dd>
        <dt>Assigned To</dt><dd>{deal.assignedToName || 'Unassigned'}</dd>
      </dl>
      <Link to="/deals" className="link-btn">&larr; Back to Deals</Link>
    </div>
  );
}
