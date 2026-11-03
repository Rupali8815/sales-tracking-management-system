import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';

export default function SalesLineChart({ data, xKey = 'month', yKey = 'total' }) {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <LineChart data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey={xKey} />
        <YAxis />
        <Tooltip />
        <Line type="monotone" dataKey={yKey} stroke="var(--color-primary, #2563eb)" strokeWidth={2} dot={false} />
      </LineChart>
    </ResponsiveContainer>
  );
}
