const ACCESS_TOKEN_KEY = 'sales_tracker_access_token';
const REFRESH_TOKEN_KEY = 'sales_tracker_refresh_token';

export const getAccessToken = () => sessionStorage.getItem(ACCESS_TOKEN_KEY);
export const getRefreshToken = () => sessionStorage.getItem(REFRESH_TOKEN_KEY);

export const setTokens = (accessToken, refreshToken) => {
  sessionStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
  sessionStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
};

const USER_KEY = 'sales_tracker_user';

export const getStoredUser = () => {
  try {
    const raw = sessionStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
};

export const setStoredUser = (user) => {
  try { sessionStorage.setItem(USER_KEY, JSON.stringify(user)); } catch { /* ignore */ }
};

export const clearTokens = () => {
  sessionStorage.removeItem(USER_KEY);
  sessionStorage.removeItem(ACCESS_TOKEN_KEY);
  sessionStorage.removeItem(REFRESH_TOKEN_KEY);
};
