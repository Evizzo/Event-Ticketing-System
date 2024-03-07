import React, { useState } from 'react';
import { Form, FormControl, Button, Container, Row, Col } from 'react-bootstrap';
import { searchEventsByName, purchaseEventTicket } from '../api/ApiService';
import { useAuth } from '../api/AuthContex';
import EventCard from './EventCard';

function Search() {
  const [searchTerm, setSearchTerm] = useState('');
  const [foundEvents, setFoundEvents] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [numberOfBought, setNumberOfBought] = useState(0);
  const authContext = useAuth();
  const isAuthenticated = authContext.isAuthenticated;

  const handleSearch = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setLoading(true);
    try {
      setMessage('');
      const response = await searchEventsByName(searchTerm);
      setFoundEvents(response.data);
      setLoading(false);
    } catch (error) {
      setFoundEvents([]);
      setLoading(false);
      setMessage('Error searching for events. Please try again.');
    }
  };

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const handlePurchase = (eventId: string) => {
    purchaseEventTicket(eventId)
      .then((response) => {
        const updatedNumberOfBought = numberOfBought + 1;
        setNumberOfBought(updatedNumberOfBought);
        
        let purchaseMessage = `Ticket purchased ${updatedNumberOfBought} time`;
        if (updatedNumberOfBought > 1) {
          purchaseMessage += 's';
        }
  
        setMessage(`${purchaseMessage}: ${response.data}`);
        
        const updatedEvents = foundEvents.map((event) => {
          if (event.id === eventId) {
            return { ...event, capacity: event.capacity - 1 };
          }
          return event;
        });
  
        setFoundEvents(updatedEvents);
      })
      .catch((error) => {
        setMessage(error.response.data.message)
        setTimeout(() => setMessage(''), 3000);
      });
  };

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col xs={12} md={6}>
          <Form onSubmit={handleSearch}>
            <Form.Group className="d-flex align-items-center">
              <FormControl
                type="text"
                placeholder="Search..."
                size="lg"
                value={searchTerm}
                onChange={handleInputChange}
              />
              <Button variant="primary" type="submit" size="lg" className="ms-2">
                Search
              </Button>
            </Form.Group>
          </Form>
        </Col>
      </Row>
      <br></br>
      {message && <div className="alert alert-warning">{message}</div>}
      <Row className="justify-content-center mt-4">
        {loading ? (
          <p>Loading events...</p>
        ) : (
          foundEvents.map((event) => (
            <EventCard
              key={event.id}
              event={event}
              isAuthenticated={isAuthenticated}
              handlePurchase={handlePurchase}
            />
          ))
        )}
      </Row>
    </Container>
  );
}

export default Search;