export const formatDate = (isoDate) => {
  if (!isoDate) return '-';
  const d = new Date(isoDate);
  return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
};

export const currentMonthYear = () => {
  const now = new Date();
  return { month: now.getMonth() + 1, year: now.getFullYear() };
};

export const monthName = (monthNumber) =>
  new Date(2000, monthNumber - 1, 1).toLocaleString('en-IN', { month: 'long' });
