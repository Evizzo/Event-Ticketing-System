import { Link } from 'react-router-dom';

interface EventCardProps {
  event: any;
  isAuthenticated: boolean;
  handlePurchase: (eventId: string) => void;
}

function EventCard(props: EventCardProps) {
  const { event, isAuthenticated, handlePurchase } = props;

  return (
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
        {event.done ? (
          <div className="position-absolute bottom-0 end-0 p-2">
            <strong style={{ color: 'red' }}>Ended</strong>
          </div>
        ) : (
          <>
            {isAuthenticated && (
              <div className="position-absolute bottom-0 end-0 p-2">
                <button
                  className="btn btn-primary"
                  onClick={() => handlePurchase(event.id)}
                >
                  Purchase: {event.capacity}
                </button>
              </div>
            )}
            {!isAuthenticated && (
              <div className="position-absolute bottom-0 end-0 p-2">
                <strong>Tickets left: <i>{event.capacity}</i></strong>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default EventCard;
