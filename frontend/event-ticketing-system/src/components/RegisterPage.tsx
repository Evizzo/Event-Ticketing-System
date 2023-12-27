import { useState, ChangeEvent, FormEvent } from 'react';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';

function RegisterPage() {
    const [firstname, setFirstName] = useState('');
    const [lastname, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();
    const [message,setMessage] = useState("")
    const authContext = useAuth();

    const handleFirstNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        setFirstName(e.target.value);
    };

    const handleLastNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        setLastName(e.target.value);
    };

    const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
        setEmail(e.target.value);
    };

    const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
    };

    const handleConfirmPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        setConfirmPassword(e.target.value);
    };

    async function handleRegister(e: FormEvent<HTMLFormElement>) {
        e.preventDefault();
    
        if (password !== confirmPassword) {
            setMessage("Passwords don't match");
            return;
        }
    
        const userData = {
            firstname: firstname,
            lastname: lastname,
            email: email,
            password: password,
        };
    
        try {            
            if (await authContext.register(userData)) {
                console.log('Registration successful');
                navigate("/")
            } else {
                alert(`Registration failed, password must have at least 7 characters and contain at least one number.`);
                return;
            }
        } catch (error: any) {
            if (error.response && error.response.data) {
                alert(error.response.data.message)
              }
            else
                alert(`Error during registration: ${error}`);
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <Card className="bg-light p-4 text-center">
                <Container>
                    <h1>Register for Event Ticketing System</h1>
                    <br />
                    <Form onSubmit={handleRegister}>
                        <Form.Group controlId="formBasicFirstName">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter first name"
                                value={firstname}
                                onChange={handleFirstNameChange}
                                required
                            />
                        </Form.Group>
                        <br />
                        <Form.Group controlId="formBasicLastName">
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter last name"
                                value={lastname}
                                onChange={handleLastNameChange}
                                required
                            />
                        </Form.Group>
                        <br />
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
                        <br />
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
                        <br />
                        <Form.Group controlId="formBasicConfirmPassword">
                            <Form.Label>Confirm Password</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Confirm Password"
                                value={confirmPassword}
                                onChange={handleConfirmPasswordChange}
                                required
                            />
                        </Form.Group>
                        <br />
                        <Button variant="primary" type="submit">
                            Register
                        </Button>
                    </Form>
                    <br />
                    <p className="mt-3">
                        Already have an account? <Link to="/login">Login</Link>
                    </p>
                </Container>
                {message && <div className="alert alert-warning">{message}</div>}
            </Card>
        </div>
    );
}

export default RegisterPage;
