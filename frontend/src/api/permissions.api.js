import axiosClient from './axiosClient';

export const listPermissions = () => axiosClient.get('/permissions');
export const createPermission = (permissionCode, description) =>
  axiosClient.post('/permissions', { permissionCode, description });
