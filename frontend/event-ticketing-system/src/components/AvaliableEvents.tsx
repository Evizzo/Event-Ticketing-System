import { useState, useEffect } from 'react';
import { retrieveAllEvents, purchaseEventTicket } from '../api/ApiService.ts';
import { Link } from 'react-router-dom';
import { useAuth } from '../api/AuthContex';

function AvailableEvents() {

  const authContext = useAuth();
  const [events, setEvents] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [message,setMessage] = useState("")
  const [numberOfBought, setNumberOfBought] = useState(1)

  useEffect(() => {
    retrieveAllEvents()
      .then((response) => {
        const sortedEvents = response.data.sort((a: { name: string }, b: { name: string }) =>
        a.name.localeCompare(b.name));
        setEvents(sortedEvents);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching events:', error);
        setLoading(false);
      });
  }, [numberOfBought]);

  const handlePurchase = (eventId: string, userId: string) => {
    purchaseEventTicket(eventId, userId)
      .then((response) => {
        setNumberOfBought(numberOfBought + 1)

        if (numberOfBought === 1){
          setMessage(`Ticket purchased ${numberOfBought} time: ${response.data}`)
        }
        else{
          setMessage(`Ticket purchased ${numberOfBought} times: ${response.data}`)
        }
      })
      .catch((error) => {
        setMessage(`Error purchasing ticket: ${error}`)
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
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default AvailableEvents;
