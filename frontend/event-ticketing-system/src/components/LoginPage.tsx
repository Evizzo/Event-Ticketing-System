import { useState, ChangeEvent, FormEvent } from 'react';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';
import { executeSendPasswordEmail } from '../api/ApiService';

function LoginPage() {
  const authContext = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showForgotPassword, setShowForgotPassword] = useState(false);
  const [forgotPasswordEmail, setForgotPasswordEmail] = useState('');
  const [forgotPasswordMessage, setForgotPasswordMessage] = useState('');
  const navigate = useNavigate();

  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleForgotPassword = () => {
    setShowForgotPassword(true);
  };

  const handleForgotPasswordEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    setForgotPasswordEmail(e.target.value);
  };

  const handleSendPasswordEmail = async () => {
    try {
      await executeSendPasswordEmail(forgotPasswordEmail);
      setForgotPasswordMessage('Email sent successfully.');
    } catch (error) {
      setForgotPasswordMessage('Email not found.');
    }
  };

  async function handleLogin(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    try {
      if (await authContext.login(email, password)) {
        navigate(`/`);
      } else {
        alert("Incorrect credentials")
      }
    } catch (error) {
      alert(`Error during login: ${error}`);
    }
  }

  return (
    <div className="d-flex justify-content-center align-items-center vh-100">
      <Card className="bg-light p-4 text-center">
        <Container>
          <h1>Login to Event Ticketing System</h1>
          <br></br>
          <Form onSubmit={handleLogin}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter email"
                value={email}
                onChange={handleEmailChange}
                required
              />
            </Form.Group>
            <br></br>
            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Password"
                value={password}
                onChange={handlePasswordChange}
                required
              />
            </Form.Group>
            <br></br>
            <Button variant="primary" type="submit">
              Login
            </Button>
          </Form>
          <br></br>
          <p className="mt-3">
            Don't have an account? <Link to="/register">Sign Up</Link>
          </p>
          <Button variant="link" onClick={handleForgotPassword}>
            Forgot password?
          </Button>
          <hr></hr>
          {showForgotPassword && (
            <div>
              <br />
              <Form>
                <Form.Group controlId="formForgotPasswordEmail">
                  <Form.Label>(Feedback from the server might take a few seconds)</Form.Label> <br></br>
                  <Form.Label>Enter your email: </Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter email"
                    value={forgotPasswordEmail}
                    onChange={handleForgotPasswordEmailChange}
                    required
                  />
                </Form.Group>
                <br></br>
                <Button variant="primary" onClick={handleSendPasswordEmail}>
                  Send Password Reset Email
                </Button>
                <br></br>
                <br></br>
                {forgotPasswordMessage && <Alert variant="info">{forgotPasswordMessage}</Alert>}
              </Form>
            </div>
          )}
        </Container>
      </Card>
    </div>
  );
}

export default LoginPage;
