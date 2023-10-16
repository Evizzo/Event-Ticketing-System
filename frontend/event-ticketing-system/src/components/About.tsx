function About() {
  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-md-6">
          <img
            src="public/ets.png"
            className="img-fluid rounded-circle"
            alt="About Us"
          />
        </div>
        <div className="col-md-6">
          <h2>About Us</h2>
          <p>
            The Event Ticketing System is a web application designed for managing and attending events. It is built using Spring Boot for the backend, backed by a database, and features a React frontend. The key functions of this application include:
          </p>

          <ol>
              <li>User Registration and Authentication: Users can securely register for accounts and authenticate using Spring Security.</li>
              <li>Event Management: Event organizers can add, update, and remove events, providing detailed information like event names, dates, locations, descriptions, and ticket prices.</li>
              <li>Ticket Purchase and Refund: Attendees can browse and purchase tickets for events, and also request refunds if needed.</li>
              <li>User-Friendly Interface: The application offers a user-friendly React frontend.</li>
          </ol>

          <p>
              The Event Ticketing System represents the integration of Spring Boot and React.
          </p>

        </div>
      </div>
    </div>
  );
}

export default About;
