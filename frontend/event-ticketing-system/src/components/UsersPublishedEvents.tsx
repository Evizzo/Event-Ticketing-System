import { useState, useEffect } from 'react';
import { retrieveAllPublishersEvents, deleteEvent, eventIsDone } from '../api/ApiService.ts';
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
  done: boolean;
}

function UsersPublishedEvents() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [message, setMessage] = useState<string>('');
  const authContext = useAuth();
  const isAuthenticated: boolean = authContext.isAuthenticated;
  const [numberOfActions, setNumberOfActions] = useState(1);

  useEffect(() => {
    retrieveAllPublishersEvents()
      .then((response: { data: Event[] }) => {
        console.log(response);
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
  }, [numberOfActions]);

  const handleDelete = (eventId: string) => {
    deleteEvent(eventId)
      .then((response: { data: string }) => {
        console.log(response);
        setNumberOfActions(numberOfActions + 1);
        setMessage('Event deleted/canceled successfully.');
      })
      .catch((error: Error) => {
        setMessage(`Error deleting/canceled event: ${error}`);
        setTimeout(() => setMessage(''), 3000);
      });
  };
  const handleDone = (eventId: string) => {
    eventIsDone(eventId)
      .then((response: { data: string }) => {
        console.log(response);
        setNumberOfActions(numberOfActions + 1);
        setMessage('Event status updated successfully.');
      })
      .catch((error: Error) => {
        setMessage(`Error updating event as done: ${error}`);
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
                <div className="card-body">
                  <div>
                    <h5 className="card-title">{event.name}</h5>
                    <p className="card-text">{event.description}</p>
                    <p className="card-text">
                      <strong>Price: ${event.ticketPrice}</strong>
                    </p>
                  </div>
                  {isAuthenticated && (
                    <div className="d-flex justify-content-between mt-3">
                      <button
                        className="btn btn-primary"
                        onClick={() => {/* Handle "Edit" functionality */}}
                      >
                        Edit
                      </button>
                      {!event.done && <button
                        className="btn btn-success"
                        onClick={() => {handleDone(event.id)}}
                      >
                        Done
                      </button>}
                      {event.done && <button
                        className="btn btn-warning"
                        onClick={() => {handleDone(event.id)}}
                      >
                        Undone
                      </button>}
                      <button
                        className="btn btn-danger"
                        onClick={() => handleDelete(event.id)}
                      >
                        Cancel/Delete
                      </button>
                    </div>
                  )
                  }
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default UsersPublishedEvents;
