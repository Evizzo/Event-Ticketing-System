import { useState, ChangeEvent, FormEvent } from 'react';
import { addNewEvent } from '../api/ApiService';

type Event = {
  name: string;
  date: string;
  location: string;
  description: string;
  capacity: number;
  ticketPrice: number;
};

function Publish() {
  const [message,setMessage] = useState("")
  const [eventData, setEventData] = useState<Event>({
    name: '',
    date: '',
    location: '',
    description: '',
    capacity: 0,
    ticketPrice: 0,
  });

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setEventData({
      ...eventData,
      [name]: name === 'capacity' || name === 'ticketPrice' ? parseFloat(value) : value,
    });
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    try {
      const response = await addNewEvent(eventData);
      console.log('Event added:', response);

      setEventData({
        name: '',
        date: '',
        location: '',
        description: '',
        capacity: 0,
        ticketPrice: 0,
      });
    } catch (error: any) {
      if (error.response && error.response.data) {
        setMessage(error.response.data.message)
      } else {
        console.error('Error adding event:', error);
      }
    }
  };

  return (
    <div className="container mt-4">
      {message && <div className="alert alert-warning">{message}</div>}
      <h1>Publish Event</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group mb-3">
          <label htmlFor="name">Event Name</label>
          <input
            type="text"
            className="form-control"
            id="name"
            name="name"
            value={eventData.name}
            onChange={handleChange}
          />
        </div>
        <div className="form-group mb-3">
          <label htmlFor="date">Event Date</label>
          <input
            type="date"
            className="form-control"
            id="date"
            name="date"
            value={eventData.date}
            onChange={handleChange}
          />
        </div>
        <div className="form-group mb-3">
          <label htmlFor="location">Location</label>
          <input
            type="text"
            className="form-control"
            id="location"
            name="location"
            value={eventData.location}
            onChange={handleChange}
          />
        </div>
        <div className="form-group mb-3">
          <label htmlFor="description">Description</label>
          <textarea
            className="form-control"
            id="description"
            name="description"
            value={eventData.description}
            onChange={handleChange}
          />
        </div>
        <div className="form-group mb-3">
          <label htmlFor="capacity">Capacity</label>
          <input
            type="number"
            className="form-control"
            id="capacity"
            name="capacity"
            value={eventData.capacity.toString()}
            onChange={handleChange}
          />
        </div>
        <div className="form-group mb-3">
          <label htmlFor="ticketPrice">Ticket Price</label>
          <input
            type="number"
            step="0.01"
            className="form-control"
            id="ticketPrice"
            name="ticketPrice"
            value={eventData.ticketPrice.toString()}
            onChange={handleChange}
          />
        </div>
        <div className="d-grid gap-2">
          <button type="submit" className="btn btn-primary">
            Publish Event
          </button>
        </div>
      </form>
    </div>
  );
}

export default Publish;
