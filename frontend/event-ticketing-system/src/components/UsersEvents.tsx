import { useState, useEffect } from 'react';
import { retrieveAllUserTickets, refoundTicket } from '../api/ApiService.ts';
import { Link } from 'react-router-dom';

function UsersEvents() {
  const [events, setEvents] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [message,setMessage] = useState("")
  const [refundAttempts, setRefundAttempts] = useState(1)

  useEffect(() => {
    retrieveAllUserTickets()
      .then((response) => {
        const sortedEvents = response.data.sort((a: { event: { name: string } }, b: { event: { name: string } }) =>
        a.event.name.localeCompare(b.event.name));
        setEvents(sortedEvents);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching events:', error);
        setLoading(false);
      });
    }, [refundAttempts]);

  const handleRefound = (ticketId: string) => {
    console.log(ticketId)
    refoundTicket(ticketId)
      .then((response) => {
        setRefundAttempts(refundAttempts + 1)
        if (refundAttempts === 1){
          setMessage(`Ticket refounded ${refundAttempts} time: ${response.data}`)
        }
        else{
          setMessage(`Ticket refounded ${refundAttempts} times: ${response.data}`)
        }
      })
      .catch((error) => {
        setRefundAttempts(refundAttempts + 1)
        setMessage(`Error refounding the ticket: ${error}`)
        setTimeout(() => setMessage(''), 3000);
      });
  };

  return (
    <div className="container mt-4">
      {loading ? (
        <p>Loading events...</p>
      ) : (
        <div className="row">
          {message && <div className="alert alert-warning alert-dismissible fade show">{message}
          </div>}
          {events.map((event) => (
            <div className="col-md-4 mb-4" key={event.id}>
              <div className="card h-100 d-flex flex-column position-relative">
              <Link to={`/events/${event.event.id}`}>
                <img
                  src="public/ets.png"
                  className="card-img-top"
                  alt={event.event.name}
                />
              </Link>
                <div className="position-absolute top-0 start-0 m-3">
                  <p className="mb-0">
                    <strong>Date:</strong> {event.event.date}
                  </p>
                </div>
                <div className="position-absolute top-0 end-0 m-3">
                  <p className="mb-0">
                    <strong>Location:</strong> {event.event.location}
                  </p>
                </div>
                <div className="card-body d-flex flex-column justify-content-between">
                  <div>
                    <h5 className="card-title">{event.event.name}</h5>
                    <p className="card-text">{event.event.description}</p>
                  </div>
                  <div className="text-left">
                    <p className="card-text">
                      <strong>Paid: ${event.event.ticketPrice}</strong>
                    </p>
                  </div>
                </div>
                <div className="position-absolute bottom-0 end-0 p-2">
                  <button
                    className="btn btn-primary"
                    onClick={() => handleRefound(event.id)}
                  >
                    Refound: {event.amount}
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

export default UsersEvents;
