export const isRequired = (value) => (value !== undefined && value !== null && String(value).trim() !== '');
export const isEmail = (value) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value || '');
export const isPhone = (value) => /^[0-9+\-\s()]{7,20}$/.test(value || '');
export const minLength = (value, len) => (value || '').length >= len;
