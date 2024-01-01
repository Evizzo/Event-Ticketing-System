# **Event Ticketing System**
This Event Ticketing System project is a full-stack application built to manage events, tickets, and users in a simplified ticketing environment.

**Backend Features**
**Controllers:** The backend features multiple controllers handling various functionalities.

EventController: Manages events, allowing creation, retrieval, deletion, and updating of events. It includes features like retrieving published events, searching by name, marking an event as done, and accessing popular events.
TicketController: Facilitates ticket purchasing and refunds for specific events.
UserController: Manages user-related operations such as user creation, retrieval, updating, and deletion. It also handles user credits, current user details, and user ticket retrieval.

**Services:** The backend employs services to handle business logic and interact with the repositories.

EventService: Provides methods for event-related functionalities like saving events, finding events by different criteria, and handling event deletions and updates.
TicketService: Manages ticket-related operations such as purchasing and refunding tickets.
UserService: Handles user-related functionalities like user creation, retrieval, updating, and deletion, along with managing user credits and ticket retrieval.
Security Configuration: Implements security measures using JWT (JSON Web Tokens) authentication. It secures endpoints and manages user authentication and authorization.

**Frontend Features**
The frontend is built with React, utilizing TypeScript, Bootstrap, HTML, and CSS to create a user-friendly interface.

**Testing**
The project includes comprehensive test coverage to ensure the reliability and stability of the application.

**Technologies Used**
Java, Spring Boot, Spring Security, Jpa/Hibernate, React, TypeScript, Bootstrap, HTML, CSS, Mockito...

This project aims to provide a scalable and secure platform for managing events, tickets, and user interactions within a ticketing system.
