import axiosClient from './axiosClient';

// payload: { dealId, amount, invoiceNumber?, saleDate? }
export const recordSale = (payload) => axiosClient.post('/sales', payload);
export const updatePaymentStatus = (id, status) =>
  axiosClient.put(`/sales/${id}/payment-status`, { status });
export const monthlySummary = (month, year) =>
  axiosClient.get('/sales/summary/monthly', { params: { month, year } });
export const yearlySummary = (year) =>
  axiosClient.get('/sales/summary/yearly', { params: { year } });
export const listSales = (params) => axiosClient.get('/sales', { params });
