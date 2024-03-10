import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import EventCard from './EventCard';
import { useAuth } from '../api/AuthContex';
import { purchaseEventTicket, retrieveTop3PopularEvents } from '../api/ApiService';

function HomePage() {
    type Event = {
        id: string;
        name: string;
        date: string;
        location: string;
        description: string;
        capacity: number;
        price: number;
      };
    const [events, setEvents] = useState<Event[]>([]);
    const [message, setMessage] = useState<string>('');
    const [numberOfBought, setNumberOfBought] = useState<number>(1);
    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;

    useEffect(() => {
        retrieveTop3PopularEvents()
          .then((response: { data: Event[] }) => {
            setEvents([...response.data]);
          })
          .catch((error: Error) => {
            console.error('Error fetching events:', error);
          });
      }, [numberOfBought]);

      const handlePurchase = (eventId: string) => {
        purchaseEventTicket(eventId, '')
          .then((response: { data: string }) => {
            setNumberOfBought(numberOfBought + 1);
    
            setMessage(
              `Ticket purchased ${numberOfBought === 1 ? '1 time' : `${numberOfBought} times`}: ${response.data}`
            );
          })
          .catch((error) => {
            setMessage(error.response.data.message)
            console.error(`Error purchasing ticket: ${error.message}`);
            setTimeout(() => setMessage(''), 3000);
          });
      };

      return (
        <div className="d-flex flex-column align-items-center vh-100">
            <br></br>
            {message && <div className="alert alert-warning">{message}</div>}
          <Card className="bg-light p-4 text-center mb-4">
            <Container>
              <h1>Welcome to the Event Ticketing System</h1>
              <p className="mb-4">
                Buy tickets for your favorite events, concerts, and more. Find and book the best seats in town.
              </p>
              <Link to="/events">
                <Button variant="primary">Explore Events</Button>
              </Link>
            </Container>
          </Card>
          <h1>Most popular events</h1>
          <br></br>
          <div className="row">
            {events.map((event: Event) => (
              <EventCard
                key={event.id}
                event={event}
                isAuthenticated={isAuthenticated}
                handlePurchase={handlePurchase}
              />
            ))}
          </div>
        </div>
      );
    }
    
    export default HomePage;