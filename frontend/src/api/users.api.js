import axiosClient from './axiosClient';

export const listUsers = (params) => axiosClient.get('/users', { params });
export const getUser = (id) => axiosClient.get(`/users/${id}`);
export const createUser = (payload) => axiosClient.post('/users', payload);
export const updateUser = (id, payload) => axiosClient.put(`/users/${id}`, payload);
export const deactivateUser = (id) => axiosClient.delete(`/users/${id}`);
