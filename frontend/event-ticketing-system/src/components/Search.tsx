import React, { useState } from 'react';
import { Form, FormControl, Button, Container, Row, Col } from 'react-bootstrap';
import { searchEventsByName, purchaseEventTicket } from '../api/ApiService';
import { Link } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';

function Search() {
  const [searchTerm, setSearchTerm] = useState('');
  const [foundEvents, setFoundEvents] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [numberOfBought, setNumberOfBought] = useState(1);
  const authContext = useAuth();

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

  const handlePurchase = (eventId: string, userId: string) => {
    purchaseEventTicket(eventId, userId)
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
        setMessage(`Error purchasing ticket: ${error}`);
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
      <Row className="justify-content-center mt-4">
        {loading ? (
          <p>Loading events...</p>
        ) : (
          foundEvents.map((event) => (
            <Col xs={12} md={4} key={event.id}>
              <div className="card mb-4 h-100 d-flex flex-column position-relative">
              <Link to={`/events/${event.id}`}>
                <img
                  src="public/ets.png"
                  className="card-img-top"
                  alt={event.name}
                />
              </Link>
                <div className="position-absolute top-0 start-0 m-3">
                  <p className="mb-0">
                    <strong>Date:</strong> {event.date}
                  </p>
                </div>
                <div className="position-absolute top-0 end-0 m-3">
                  <p className="mb-0">
                    <strong>Location:</strong> {event.location}
                  </p>
                </div>
                <div className="card-body d-flex flex-column justify-content-between">
                  <div>
                    <h5 className="card-title">{event.name}</h5>
                    <p className="card-text">{event.description}</p>
                  </div>
                  <div className="text-left">
                    <p className="card-text">
                      <strong>Price: ${event.ticketPrice}</strong>
                    </p>
                  </div>
                </div>
                <div className="position-absolute bottom-0 end-0 p-2">
                  <button
                    className="btn btn-primary"
                    onClick={() => handlePurchase(event.id, authContext.userId)}
                  >
                    Purchase: {event.capacity}
                  </button>
                </div>
              </div>
            </Col>
          ))
        )}
      </Row>
      {message && <div className="alert alert-warning">{message}</div>}
    </Container>
  );
}

export default Search;