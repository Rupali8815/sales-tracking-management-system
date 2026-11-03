import { configureStore } from '@reduxjs/toolkit';
import authReducer from './auth.slice';
import userReducer from './user.slice';
import leadReducer from './lead.slice';
import dealReducer from './deal.slice';
import salesReducer from './sales.slice';
import targetReducer from './target.slice';

export const store = configureStore({
  reducer: {
    auth: authReducer,
    users: userReducer,
    leads: leadReducer,
    deals: dealReducer,
    sales: salesReducer,
    targets: targetReducer,
  },
});
