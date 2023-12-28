import { useState, useEffect } from 'react';
import { retrieveAllPublishersEvents, deleteEvent } from '../api/ApiService.ts';
import { Link } from 'react-router-dom';
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

function UsersPublishedEvents() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [message, setMessage] = useState<string>('');
  const authContext = useAuth();
  const isAuthenticated: boolean = authContext.isAuthenticated;
  const [numberOfDeleted, setNumberOfDeleted] = useState(1)

  useEffect(() => {
    retrieveAllPublishersEvents()
      .then((response: { data: Event[] }) => {
        const sortedEvents = response.data.sort((a, b) =>
          a.name.localeCompare(b.name)
        );
        setEvents(sortedEvents);
        setLoading(false);
      })
      .catch((error: Error) => {
        console.error('Error fetching publishers events:', error);
        setLoading(false);
      });
  }, [numberOfDeleted]);

  const handleDelete = (eventId: string) => {
    deleteEvent(eventId)
      .then((response: { data: string }) => {
        console.log(response)
        setNumberOfDeleted(numberOfDeleted + 1)
        setMessage("Event deleted successfouly.")
      })
      .catch((error: Error) => {
        setMessage(`Error deleting event: ${error}`);
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
            <div className="col-md-4 mb-4" key={event.id}>
              <div className="card h-100 d-flex flex-column position-relative">
                <Link to={`/events/${event.id}`}>
                  <img src="public/ets.png" className="card-img-top" alt={event.name} />
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
                {isAuthenticated && (
                  <div className="position-absolute bottom-0 end-0 p-2">
                    <button
                      className="btn btn-danger"
                      onClick={() => handleDelete(event.id)}
                    >
                      Delete event
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default UsersPublishedEvents;
