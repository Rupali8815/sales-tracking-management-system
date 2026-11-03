import axiosClient from './axiosClient';

export const listLeads = (params) => axiosClient.get('/leads', { params });
export const getLead = (id) => axiosClient.get(`/leads/${id}`);
export const createLead = (payload) => axiosClient.post('/leads', payload);
export const updateLead = (id, payload) => axiosClient.put(`/leads/${id}`, payload);
export const addLeadActivity = (id, activityType, notes) =>
  axiosClient.post(`/leads/${id}/activities`, { activityType, notes });
export const getLeadActivities = (id) => axiosClient.get(`/leads/${id}/activities`);
