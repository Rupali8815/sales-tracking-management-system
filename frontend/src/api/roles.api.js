import axiosClient from './axiosClient';

export const listRoles = () => axiosClient.get('/roles');
export const createRole = (roleName, description) =>
  axiosClient.post('/roles', { roleName, description });
export const assignPermissions = (roleId, permissionIds) =>
  axiosClient.put(`/roles/${roleId}/permissions`, { permissionIds });
