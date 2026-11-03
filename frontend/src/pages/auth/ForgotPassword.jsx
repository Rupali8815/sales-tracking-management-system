import { useState } from 'react';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';

export default function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: wire to a password-reset endpoint once added to AuthController
    setSent(true);
  };

  if (sent) {
    return <p>If an account exists for {email}, a reset link has been sent.</p>;
  }

  return (
    <>
    <h1 className="auth-title">Reset your password</h1>
    <form onSubmit={handleSubmit} className="auth-form">
      <Input label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
      <Button type="submit" full>Send Reset Link</Button>
    </form>
    </>
  );
}
