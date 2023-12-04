import { useEffect, useState } from 'react';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Spinner from 'react-bootstrap/Spinner';
import { retrieveUserById } from '../api/ApiService';
import { useAuth } from '../api/AuthContex';

function UserPage() {
  const [userData, setUserData] = useState({
    firstname: '',
    lastname: '',
    email: '',
    credits: 0,
  });
  const [isLoading, setIsLoading] = useState(true);
  const authContext = useAuth();

  useEffect(() => {
    const userId = authContext.userId

    retrieveUserById(userId)
      .then((response) => {
        setUserData(response.data);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching user data:', error);
        setIsLoading(false);
      });
  }, []);

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
          </Card.Body>
        </Card>
      )}
    </Container>
  );
}

export default UserPage;
