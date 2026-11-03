import Loader from '../common/Loader';

/**
 * Generic table renderer.
 * columns: [{ key, header, render?: (row) => node }]
 */
export default function DataTable({ columns, rows, loading, emptyMessage = 'No records found' }) {
  if (loading) return <Loader label="Loading data..." />;

  if (!rows || rows.length === 0) {
    return <div className="table-empty">{emptyMessage}</div>;
  }

  return (
    <div className="table-wrap">
    <table className="data-table">
      <thead>
        <tr>
          {columns.map((col) => (
            <th key={col.key}>{col.header}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {rows.map((row, idx) => (
          <tr key={row.id ?? idx}>
            {columns.map((col) => (
              <td key={col.key}>{col.render ? col.render(row) : row[col.key]}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
    </div>
  );
}
