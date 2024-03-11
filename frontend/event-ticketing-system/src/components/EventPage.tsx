import { useState, useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { retrieveEventById, purchaseEventTicket, getUserTicketByEventId, convertEventPriceCurrency, likeEvent, dislikeEvent } from '../api/ApiService.ts';
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
  publisherEmail: string;
  likes: number;
  dislikes: number;
}

function EventPage() {
  const { eventId } = useParams<{ eventId: string }>();
  const [event, setEvent] = useState<Event | null>(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [userTicket, setUserTicket] = useState<string | null>(null);
  const authContext = useAuth();
  const isAuthenticated = authContext.isAuthenticated;
  const [currencyCode, setCurrencyCode] = useState<string>('USD');
  const [codeName, setCodeName] = useState('');

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

  const handlePurchase = (eventId: string, codeName: string) => {
    purchaseEventTicket(eventId, codeName)
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

  const handleCurrencyCodeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCurrencyCode(event.target.value);
  };

  const handleConvert = () => {
    convertEventPriceCurrency(currencyCode, event!.ticketPrice)
      .then((response) => {
        alert(`${response.data} ${currencyCode}`)
        console.log(response)
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
  };
  function handleLikeEvent(id: string): void {
    likeEvent(id)
      .then(() => {
        retrieveEventById(id)
          .then((response) => {
            setEvent(response.data);
          })
          .catch((error) => {
            console.error('Error fetching event:', error);
          });
      })
      .catch((error) => {
        console.error('Error liking event:', error);
      });
  }
  
  function handleDislikeEvent(id: string): void {
    dislikeEvent(id)
      .then(() => {
        retrieveEventById(id)
          .then((response) => {
            setEvent(response.data);
          })
          .catch((error) => {
            console.error('Error fetching event:', error);
          });
      })
      .catch((error) => {
        console.error('Error disliking event:', error);
      });
  }
  
  

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
              <input type="text" value={currencyCode} onChange={handleCurrencyCodeChange} placeholder="Currency Code" />
              <button className="btn btn-primary ml-2" onClick={handleConvert}>Convert</button>
            </p>
            <p>
              <strong>Ticket ID:</strong> {userTicket || 'Not purchased'}
            </p>
            <p>
              <strong>Publisher:</strong> <Link to={`/user-profile/${event.publisherEmail}`}>{event.publisherEmail}</Link>
            </p>
            <p className="mb-2">
             <span style={{ color: 'green' }}><strong>Likes:</strong> {event.likes}</span>
          </p>
          <p className="mb-2">
            <span style={{ color: 'red' }}><strong>Dislikes:</strong> {event.dislikes}</span>
          </p>
          {isAuthenticated && (
              <div className="btn-group">
                <button className="btn btn-sm btn-success" onClick={() => handleLikeEvent(event.id)}>
                  Like
                </button>
                <button className="btn btn-sm btn-danger" onClick={() => handleDislikeEvent(event.id)}>
                  Dislike
                </button>
              </div>
            )}
            {event.done ? (
            <div className="text-center">
              <strong style={{ color: 'red' }}>Done</strong>
            </div>
            ) : (<>
            {isAuthenticated &&
              <div className="text-center">
                <div className="form-group">
                  <label>Code Name:</label>
                  <input type="text" className="form-control" value={codeName} onChange={(e) => setCodeName(e.target.value)} />
                </div>
                <button
                  className="btn btn-primary"
                  onClick={() =>
                    handlePurchase(event.id, codeName)
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