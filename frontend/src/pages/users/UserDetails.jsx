import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import * as usersApi from '../../api/users.api';
import Loader from '../../components/common/Loader';
import { formatDate } from '../../utils/date.util';

export default function UserDetails() {
  const { id } = useParams();
  const [user, setUser] = useState(null);

  useEffect(() => {
    usersApi.getUser(id).then(({ data }) => setUser(data));
  }, [id]);

  if (!user) return <Loader label="Loading user..." />;

  return (
    <div className="card">
      <h1 className="page-title">{user.name}</h1>
      <dl className="detail-list">
        <dt>Email</dt><dd>{user.email}</dd>
        <dt>Phone</dt><dd>{user.phone || '-'}</dd>
        <dt>Status</dt><dd>{user.status}</dd>
        <dt>Roles</dt><dd>{(user.roles || []).join(', ') || '-'}</dd>
        <dt>Created</dt><dd>{formatDate(user.createdAt)}</dd>
      </dl>
      <Link to={`/users/${id}/edit`} className="link-btn">Edit User</Link>
    </div>
  );
}
