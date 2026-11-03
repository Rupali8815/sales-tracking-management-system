import { createSlice } from '@reduxjs/toolkit';

const targetSlice = createSlice({
  name: 'targets',
  initialState: { list: [], achievement: 0, loading: false },
  reducers: {
    setTargets: (state, action) => {
      state.list = action.payload;
    },
    setAchievement: (state, action) => {
      state.achievement = action.payload;
    },
    setTargetsLoading: (state, action) => {
      state.loading = action.payload;
    },
  },
});

export const { setTargets, setAchievement, setTargetsLoading } = targetSlice.actions;
export default targetSlice.reducer;
