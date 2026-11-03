import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { store } from './redux/store';
import { AuthProvider } from './context/AuthContext';
import { PermissionProvider } from './context/PermissionContext';
import { AppProvider } from './context/AppContext';
import App from './App';
import ErrorBoundary from './components/common/ErrorBoundary';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ErrorBoundary>
    <Provider store={store}>
      <BrowserRouter>
        <AuthProvider>
          <PermissionProvider>
            <AppProvider>
              <App />
            </AppProvider>
          </PermissionProvider>
        </AuthProvider>
      </BrowserRouter>
    </Provider>
    </ErrorBoundary>
  </React.StrictMode>
);
