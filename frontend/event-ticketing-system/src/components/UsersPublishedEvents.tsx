import { useState, useEffect } from 'react';
import { retrieveAllPublishersEvents, deleteEvent, eventIsDone } from '../api/ApiService.ts';
import { Link, useNavigate } from 'react-router-dom';
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
type SortCriteria = 'name' | 'price' | 'date';

function UsersPublishedEvents() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [message, setMessage] = useState<string>('');
  const authContext = useAuth();
  const isAuthenticated: boolean = authContext.isAuthenticated;
  const [numberOfActions, setNumberOfActions] = useState(1);
  const navigate = useNavigate();
  const [sortCriteria, setSortCriteria] = useState<SortCriteria>('name');

  useEffect(() => {
    retrieveAllPublishersEvents(sortCriteria)
      .then((response: { data: Event[] }) => {
        console.log(response);
        setEvents([...response.data]);
        setLoading(false);
      })
      .catch((error: Error) => {
        console.error('Error fetching publishers events:', error);
        setLoading(false);
      });
  }, [numberOfActions, sortCriteria]);
  const handleSortChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSortCriteria(event.target.value as SortCriteria);
  };
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
  const handleEdit = (eventId: string) => {
    navigate(`/event/${eventId}`);
  };
  return (
    <div className="container mt-4">
      {loading ? (
        <p>Loading events...</p>
      ) : (
        <div className="row">
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
                    <h5 className="card-title"><strong>{event.name}</strong></h5>
                    <p className="card-text">{event.description}</p>
                    <p className="card-text">
                      <strong>Price: ${event.ticketPrice}</strong>
                    </p>
                  </div>
                  {isAuthenticated && (
                    <div className="d-flex justify-content-between mt-3">
                      <button
                        className="btn btn-primary"
                        onClick={() => handleEdit(event.id)} 
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
