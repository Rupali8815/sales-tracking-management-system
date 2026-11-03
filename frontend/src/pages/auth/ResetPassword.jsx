import { useState } from 'react';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';

export default function ResetPassword() {
  const [password, setPassword] = useState('');
  const [confirm, setConfirm] = useState('');
  const [error, setError] = useState('');
  const [done, setDone] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (password !== confirm) {
      setError('Passwords do not match.');
      return;
    }
    // TODO: wire to a password-reset confirmation endpoint
    setDone(true);
  };

  if (done) return <p>Your password has been reset. You can now sign in.</p>;

  return (
    <>
    <h1 className="auth-title">Choose a new password</h1>
    <form onSubmit={handleSubmit} className="auth-form">
      <Input label="New Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
      <Input label="Confirm Password" type="password" value={confirm} onChange={(e) => setConfirm(e.target.value)} />
      {error && <p className="form-error">{error}</p>}
      <Button type="submit" full>Reset Password</Button>
    </form>
    </>
  );
}
