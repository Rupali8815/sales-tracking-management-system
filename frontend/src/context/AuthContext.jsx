import { createContext, useState, useCallback, useMemo } from 'react';
import * as authApi from '../api/auth.api';
import { setTokens, clearTokens, getAccessToken, getStoredUser, setStoredUser } from '../utils/token.util';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  // Restore user after a page refresh (tokens + user live in sessionStorage)
  const [user, setUser] = useState(() => (getAccessToken() ? getStoredUser() : null));
  const [loading, setLoading] = useState(false);

  const login = useCallback(async (credentials) => {
    setLoading(true);
    try {
      const { data } = await authApi.login(credentials);
      setTokens(data.accessToken, data.refreshToken);
      const loggedIn = { id: data.userId, name: data.name, email: data.email, roles: data.roles };
      setStoredUser(loggedIn);
      setUser(loggedIn);
      return data;
    } finally {
      setLoading(false);
    }
  }, []);

  const logout = useCallback(async () => {
    try {
      await authApi.logout();
    } finally {
      clearTokens();
      setUser(null);
    }
  }, []);

  // Depends on `user`, so it updates right after login/logout
  const isAuthenticated = Boolean(user && getAccessToken());

  const value = useMemo(
    () => ({ user, login, logout, loading, isAuthenticated }),
    [user, login, logout, loading, isAuthenticated]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
