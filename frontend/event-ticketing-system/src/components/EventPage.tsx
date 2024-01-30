import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { retrieveEventById, purchaseEventTicket, getUserTicketByEventId } from '../api/ApiService.ts';
import { useAuth } from '../api/AuthContex';
import CommentBox from './CommentBox.tsx';

interface Event {
  id: string;
  name: string;
  date: string;
  location: string;
  description: string;
  ticketPrice: number;
  capacity: number;
  done: boolean;
  commentCount: number;
}

function EventPage() {
  const { eventId } = useParams<{ eventId: string }>();
  const [event, setEvent] = useState<Event | null>(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [userTicket, setUserTicket] = useState<string | null>(null);
  const authContext = useAuth();
  const isAuthenticated = authContext.isAuthenticated;

  useEffect(() => {
    if (eventId) {
      retrieveEventById(eventId)
        .then((response) => {
          setEvent(response.data);
          setLoading(false);

          if (isAuthenticated) {
            getUserTicketByEventId(eventId)
              .then((ticketResponse) => {
                setUserTicket(ticketResponse.data.id);
              })
              .catch((error) => {
                console.error('Error fetching user ticket:', error);
              });
          }
        })
        .catch((error) => {
          console.error('Error fetching event:', error);
          setLoading(false);
        });
    }
  }, [eventId, isAuthenticated]);

  const handlePurchase = (eventId: string) => {
    purchaseEventTicket(eventId)
      .then((response) => {
        setMessage(`${response.data}`);
        updateEventCapacity();
      })
      .catch((error) => {
        setMessage(error.response.data.message)
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
      {message && <div className="alert alert-info mt-3">{message}</div>}
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
            <h3 className="card-title"><strong>{event.name}</strong></h3>
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
            <p>
              <strong>Ticket ID:</strong> {userTicket || 'Not purchased'}
            </p>
            {event.done ? (
            <div className="text-center">
              <strong style={{ color: 'red' }}>Done</strong>
            </div>
            ) : (<>
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
            </>)}
          </div>
          {event.done && <CommentBox eventId={event.id} updateEvent={updateEventCapacity} commentCount={event.commentCount} />}
        </div>
        
      ) : (
        <p>Event not found.</p>
      )}      
    </div>
  );
}

export default EventPage;