import { useState, useEffect } from 'react';
import { retrieveAllEvents, purchaseEventTicket } from '../api/ApiService.ts';
import { useAuth } from '../api/AuthContex';
import EventCard from './EventCard';

function AvailableEvents() {
  const [events, setEvents] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [numberOfBought, setNumberOfBought] = useState(1);
  const authContext = useAuth();
  const isAuthenticated = authContext.isAuthenticated;

  useEffect(() => {
    retrieveAllEvents()
      .then((response) => {
        const sortedEvents = response.data.sort((a: { name: string }, b: { name: string }) =>
          a.name.localeCompare(b.name)
        );
        setEvents(sortedEvents);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching events:', error);
        setLoading(false);
      });
  }, [numberOfBought]);

  const handlePurchase = (eventId: string) => {
    purchaseEventTicket(eventId)
      .then((response) => {
        setNumberOfBought(numberOfBought + 1);

        if (numberOfBought === 1) {
          setMessage(`Ticket purchased ${numberOfBought} time: ${response.data}`);
        } else {
          setMessage(`Ticket purchased ${numberOfBought} times: ${response.data}`);
        }
      })
      .catch((error) => {
        setMessage(`Not enough credits.`);
        console.error(`Error purchasing ticket: ${error.message}`);
        setTimeout(() => setMessage(''), 3000);
      });
  };

  return (
    <div className="container mt-4">
      {loading ? (
        <p>Loading events...</p>
      ) : (
        <div className="row">
          {message && <div className="alert alert-warning">{message}</div>}
          {events.map((event) => (
            <EventCard
              key={event.id}
              event={event}
              isAuthenticated={isAuthenticated}
              handlePurchase={handlePurchase}
            />
          ))}
        </div>
      )}
    </div>
  );
}

export default AvailableEvents;
