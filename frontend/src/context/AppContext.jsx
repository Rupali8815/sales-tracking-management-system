import { createContext, useState, useCallback, useMemo } from 'react';

export const AppContext = createContext(null);

export function AppProvider({ children }) {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [toast, setToast] = useState(null);

  const toggleSidebar = useCallback(() => setSidebarOpen((prev) => !prev), []);

  const showToast = useCallback((message, type = 'info') => {
    setToast({ message, type, id: Date.now() });
    setTimeout(() => setToast(null), 4000);
  }, []);

  const value = useMemo(
    () => ({ sidebarOpen, toggleSidebar, toast, showToast }),
    [sidebarOpen, toggleSidebar, toast, showToast]
  );

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
}
