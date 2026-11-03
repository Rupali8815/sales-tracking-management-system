import axiosClient from './axiosClient';

export const salesByUser = (month, year) =>
  axiosClient.get('/reports/sales-by-user', { params: { month, year } });
export const salesByPeriod = (year) =>
  axiosClient.get('/reports/sales-by-period', { params: { year } });
export const conversionRatio = () => axiosClient.get('/reports/conversion-ratio');
export const lostDeals = () => axiosClient.get('/reports/lost-deals');
