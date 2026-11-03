import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import { isRequired, isEmail } from '../../utils/validation.util';

export default function Login() {
  const { login, loading } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!isRequired(form.email) || !isEmail(form.email)) {
      setError('Please enter a valid email address.');
      return;
    }
    if (!isRequired(form.password)) {
      setError('Password is required.');
      return;
    }

    try {
      await login(form);
      navigate('/dashboard');
    } catch (err) {
      if (!err.response) {
        setError('Cannot reach the server. Is the backend running on the configured port?');
      } else {
        const status = err.response.status;
        const msg = err.response.data?.message;
        setError(msg ? `${msg} (HTTP ${status})` : `Login failed (HTTP ${status}). Check the backend console.`);
      }
    }
  };

  return (
    <>
    <h1 className="auth-title">Sign in</h1>
    <p className="auth-sub">Use the email and password your admin gave you.</p>
    <form onSubmit={handleSubmit} className="auth-form">
      <Input label="Email" name="email" type="email" value={form.email} onChange={handleChange} />
      <Input label="Password" name="password" type="password" value={form.password} onChange={handleChange} />
      {error && <p className="form-error">{error}</p>}
      <Button type="submit" full disabled={loading}>{loading ? 'Signing in...' : 'Sign In'}</Button>
      <a className="auth-link" href="/forgot-password">Forgot password?</a>
    </form>
    </>
  );
}
