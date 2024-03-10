import { useState, useEffect } from 'react';
import { retrieveAllEvents, purchaseEventTicket } from '../api/ApiService.ts';
import { useAuth } from '../api/AuthContex';
import EventCard from './EventCard';

type SortCriteria = 'name' | 'price' | 'date';

function AvailableEvents() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [message, setMessage] = useState<string>('');
  const [numberOfBought, setNumberOfBought] = useState<number>(1);
  const [sortCriteria, setSortCriteria] = useState<SortCriteria>('name');
  const authContext = useAuth();
  const isAuthenticated = authContext.isAuthenticated;

  type Event = {
    id: string;
    name: string;
    date: string;
    location: string;
    description: string;
    capacity: number;
    price: number;
  };
  
  useEffect(() => {
    retrieveAllEvents(sortCriteria)
      .then((response: { data: Event[] }) => {
        setEvents([...response.data]);
        setLoading(false);
      })
      .catch((error: Error) => {
        console.error('Error fetching events:', error);
        setLoading(false);
      });
  }, [numberOfBought, sortCriteria]);

  const handleSortChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSortCriteria(event.target.value as SortCriteria);
  };

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
    <div className="container mt-4">
      <div className="row mb-3">
        <label htmlFor="sortCriteria" className="form-label me-2">
          Sort By:
        </label>
        <select
          id="sortCriteria"
          className="form-select"
          value={sortCriteria}
          onChange={handleSortChange}
        >
          <option value="name">Name</option>
          <option value="price">Price</option>
          <option value="date">Date</option>
        </select>
      </div>
      {loading ? (
        <p>Loading events...</p>
      ) : (
        <div className="row">
          {message && <div className="alert alert-warning">{message}</div>}
          {events.map((event: Event) => (
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