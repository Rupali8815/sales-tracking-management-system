export default function Select({ label, options = [], error, placeholder, ...rest }) {
  return (
    <div className="form-field">
      {label && <label className="form-label">{label}</label>}
      <select className={`form-input${error ? ' form-input-error' : ''}`} {...rest}>
        {placeholder && <option value="">{placeholder}</option>}
        {options.map((opt) => (
          <option key={opt.value ?? opt} value={opt.value ?? opt}>
            {opt.label ?? opt}
          </option>
        ))}
      </select>
      {error && <span className="form-error">{error}</span>}
    </div>
  );
}
