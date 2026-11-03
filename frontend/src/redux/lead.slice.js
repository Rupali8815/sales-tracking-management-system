import { createSlice } from '@reduxjs/toolkit';

const leadSlice = createSlice({
  name: 'leads',
  initialState: { list: [], totalPages: 0, loading: false },
  reducers: {
    setLeads: (state, action) => {
      state.list = action.payload.content;
      state.totalPages = action.payload.totalPages;
    },
    setLeadsLoading: (state, action) => {
      state.loading = action.payload;
    },
  },
});

export const { setLeads, setLeadsLoading } = leadSlice.actions;
export default leadSlice.reducer;
