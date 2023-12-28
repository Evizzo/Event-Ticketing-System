import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { retrieveEventById, purchaseEventTicket } from '../api/ApiService.ts';
import { useAuth } from '../api/AuthContex';

interface Event {
  id: string;
  name: string;
  date: string;
  location: string;
  description: string;
  ticketPrice: number;
  capacity: number;
}

function EventPage() {
  const { eventId } = useParams<{ eventId: string }>();
  const [event, setEvent] = useState<Event | null>(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const authContext = useAuth();
  const isAuthenticated = authContext.isAuthenticated;

  useEffect(() => {
    if (eventId) {
      retrieveEventById(eventId)
        .then((response) => {
          setEvent(response.data);
          setLoading(false);
        })
        .catch((error) => {
          console.error('Error fetching event:', error);
          setLoading(false);
        });
    }
  }, [eventId]);

  const handlePurchase = (eventId: string) => {
    purchaseEventTicket(eventId)
      .then((response) => {
        setMessage(`Ticket purchased successfully: ${response.data}`);
        updateEventCapacity();
      })
      .catch((error) => {
        setMessage(`Not enought credits.`)
        console.error(`Error purchasing ticket: ${error}`)
        setTimeout(() => setMessage(''), 3000);
      });
  };

  const updateEventCapacity = () => {
    if (event) {
      setEvent((prevEvent) => ({
        ...prevEvent!,
        capacity: prevEvent!.capacity - 1,
      }));
    }
  };

  return (
    <div className="container mt-4">
      {loading ? (
        <p>Loading event details...</p>
      ) : event ? (
        <div className="card h-100 d-flex flex-column position-relative">
            <img
              src="/ets.png"
              className="card-img-top"
              alt={event.name}
              style={{
                width: '50%',
                display: 'block',
                margin: '0 auto',
              }}
            />
          <div className="card-body">
            <h5 className="card-title">{event.name}</h5>
            <p className="card-text">{event.description}</p>
            <p>
              <strong>Date:</strong> {event.date}
            </p>
            <p>
              <strong>Location:</strong> {event.location}
            </p>
            <p>
              <strong>Price:</strong> ${event.ticketPrice}
            </p>
            {isAuthenticated &&
              <div className="text-center">
                <button
                  className="btn btn-primary"
                  onClick={() =>
                    handlePurchase(event.id)
                  }
                >
                  Purchase: {event.capacity}
                </button>
              </div>
            }
            {!isAuthenticated &&
              <div className="text-center">
                <strong>Tickets left: <i>{event.capacity}</i></strong>
              </div>
            }
          </div>
        </div>
      ) : (
        <p>Event not found.</p>
      )}
      {message && <div className="alert alert-info mt-3">{message}</div>}
    </div>
  );
}

export default EventPage;
