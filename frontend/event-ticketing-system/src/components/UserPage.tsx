import { useEffect, useState, ChangeEvent } from 'react';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Spinner from 'react-bootstrap/Spinner';
import { retrieveCurrentUser, deleteCurrentUser, changePassword } from '../api/ApiService'; // Importing changePassword function
import { Button, Form } from 'react-bootstrap';
import { useAuth } from '../api/AuthContex';

function UserPage() {
  const authContext = useAuth();

  interface UserData {
    firstname: string;
    lastname: string;
    email: string;
    credits: number;
  }

  const [userData, setUserData] = useState<UserData>({
    firstname: '',
    lastname: '',
    email: '',
    credits: 0,
  });
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [currentPassword, setCurrentPassword] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');

  useEffect(() => {
    retrieveCurrentUser()
      .then((response) => {
        setUserData(response.data);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching user data:', error);
        setIsLoading(false);
      });
  }, []);

  const handleDeleteAccount = () => {
    deleteCurrentUser()
      .then(() => {
        authContext.logout();
        alert('Account deleted successfully');
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
  };

  const handleChangePassword = () => {
    changePassword(currentPassword, newPassword)
      .then(() => {
        alert('Password changed successfully');
        setCurrentPassword('');
        setNewPassword('');
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
  };

  const handleCurrentPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    setCurrentPassword(e.target.value);
  };

  const handleNewPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    setNewPassword(e.target.value);
  };

  return (
    <Container className="mt-4">
      <h1>User Information</h1>
      {isLoading ? (
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      ) : (
        <Card>
          <Card.Body>
            <Card.Title>User Details</Card.Title>
            <Card.Text>
              <strong>First Name:</strong> {userData.firstname}<br />
              <strong>Last Name:</strong> {userData.lastname}<br />
              <strong>Email:</strong> {userData.email}<br />
              <strong>Credits:</strong> {userData.credits}<br />
            </Card.Text>
            <Form>
              <Form.Group controlId="currentPassword">
                <Form.Label>Current Password</Form.Label>
                <Form.Control type="password" value={currentPassword} onChange={handleCurrentPasswordChange} />
              </Form.Group>
              <Form.Group controlId="newPassword">
                <Form.Label>New Password</Form.Label>
                <Form.Control type="password" value={newPassword} onChange={handleNewPasswordChange} />
              </Form.Group>
              <Button variant="primary" onClick={handleChangePassword}>
                Change Password
              </Button>
            </Form>
            <br></br>
            <Button variant="danger" onClick={handleDeleteAccount}>
              Delete your account
            </Button>
          </Card.Body>
        </Card>
      )}
    </Container>
  );
}

export default UserPage;
