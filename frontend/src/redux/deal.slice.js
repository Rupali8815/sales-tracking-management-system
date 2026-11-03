import { createSlice } from '@reduxjs/toolkit';

const dealSlice = createSlice({
  name: 'deals',
  initialState: { list: [], totalPages: 0, loading: false },
  reducers: {
    setDeals: (state, action) => {
      state.list = action.payload.content;
      state.totalPages = action.payload.totalPages;
    },
    setDealsLoading: (state, action) => {
      state.loading = action.payload;
    },
  },
});

export const { setDeals, setDealsLoading } = dealSlice.actions;
export default dealSlice.reducer;
