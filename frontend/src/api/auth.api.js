import axiosClient from './axiosClient';

export const login = (credentials) => axiosClient.post('/auth/login', credentials);
export const logout = () => axiosClient.post('/auth/logout');
export const refresh = (refreshToken) =>
  axiosClient.post('/auth/refresh', null, { params: { refreshToken } });
