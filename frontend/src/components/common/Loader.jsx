export default function Loader({ label = 'Loading...' }) {
  return (
    <div className="loader-wrap">
      <div className="loader-spinner" />
      <span>{label}</span>
    </div>
  );
}
