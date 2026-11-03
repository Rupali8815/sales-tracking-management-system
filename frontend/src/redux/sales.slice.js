import { createSlice } from '@reduxjs/toolkit';

const salesSlice = createSlice({
  name: 'sales',
  initialState: { monthlyTotal: 0, yearlyTotal: 0, loading: false },
  reducers: {
    setMonthlyTotal: (state, action) => {
      state.monthlyTotal = action.payload;
    },
    setYearlyTotal: (state, action) => {
      state.yearlyTotal = action.payload;
    },
    setSalesLoading: (state, action) => {
      state.loading = action.payload;
    },
  },
});

export const { setMonthlyTotal, setYearlyTotal, setSalesLoading } = salesSlice.actions;
export default salesSlice.reducer;
