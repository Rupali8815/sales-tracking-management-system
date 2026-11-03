import axiosClient from './axiosClient';

// payload: { userId, month, year, amount }
export const assignTarget = (payload) => axiosClient.post('/targets', payload);
export const listTargets = (userId, year) =>
  axiosClient.get('/targets', { params: { userId, year } });
export const getAchievement = (userId, month, year) =>
  axiosClient.get('/targets/achievement', { params: { userId, month, year } });
