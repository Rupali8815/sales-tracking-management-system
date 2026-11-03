import axiosClient from './axiosClient';

export const listDeals = (params) => axiosClient.get('/deals', { params });
export const convertLeadToDeal = (payload) => axiosClient.post('/deals', payload);
export const updateDeal = (id, payload) => axiosClient.put(`/deals/${id}`, payload);
export const closeDeal = (id, outcome) =>
  axiosClient.put(`/deals/${id}/close`, null, { params: { outcome } });
