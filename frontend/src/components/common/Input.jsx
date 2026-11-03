export default function Input({ label, error, ...rest }) {
  return (
    <div className="form-field">
      {label && <label className="form-label">{label}</label>}
      <input className={`form-input${error ? ' form-input-error' : ''}`} {...rest} />
      {error && <span className="form-error">{error}</span>}
    </div>
  );
}
