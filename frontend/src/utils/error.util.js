// Turns an axios error into a message the user can read.
export const getErrorMessage = (err, fallback = 'Something went wrong.') => {
  if (!err) return fallback;
  if (!err.response) return 'Cannot reach the server. Is the backend running?';
  const { status, data } = err.response;
  if (data?.fieldErrors && typeof data.fieldErrors === 'object') {
    const text = Object.entries(data.fieldErrors).map(([f, m]) => `${f}: ${m}`).join(', ');
    if (text) return text;
  }
  if (data?.message) return data.message;
  if (status === 403) return 'You do not have permission to do this.';
  return `${fallback} (HTTP ${status})`;
};

// Safe number helpers for API values that can be null / '' / non-numeric.
export const toNumber = (value, fallback = 0) => {
  const n = Number(value);
  return Number.isFinite(n) ? n : fallback;
};

export const formatINR = (value) => `₹${toNumber(value).toLocaleString('en-IN')}`;
