export default function TableActions({ onEdit, onDelete, canEdit = true, canDelete = true }) {
  return (
    <div className="table-actions">
      {onEdit && canEdit && <button className="link-btn" onClick={onEdit}>Edit</button>}
      {onDelete && canDelete && <button className="link-btn link-btn-danger" onClick={onDelete}>Delete</button>}
    </div>
  );
}
