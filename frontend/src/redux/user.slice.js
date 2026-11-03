import { createSlice } from '@reduxjs/toolkit';

const userSlice = createSlice({
  name: 'users',
  initialState: { list: [], totalPages: 0, loading: false },
  reducers: {
    setUsers: (state, action) => {
      state.list = action.payload.content;
      state.totalPages = action.payload.totalPages;
    },
    setUsersLoading: (state, action) => {
      state.loading = action.payload;
    },
  },
});

export const { setUsers, setUsersLoading } = userSlice.actions;
export default userSlice.reducer;
