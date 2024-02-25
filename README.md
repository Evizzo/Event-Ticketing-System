# **Event Ticketing System**

This system provides a platform for managing events, ticket purchases, user interactions, and more. 
Below, you'll find an overview of the controllers in this project along with their functionalities.

# **Tools used**

**Frontend**: Html, Css, React, TypeScript, BootStrap...

**Backend**: Java, Spring Boot, Spring Security, Jpa/Hibernate...

**Database**: MySQL.

**Testing**: Mockito, JUnit.

**Other**: Docker.

# **Overview:**

**UserController**

Manages user-related operations such as retrieving user credits, adding new users, retrieving user details, updating user information, deleting users, 
retrieving user tickets, and changing passwords.

**TicketController**

Handles ticket-related operations including purchasing tickets for events, refunding tickets, and retrieving user tickets for specific events.

**NotificationController**

Manages notification-related operations such as retrieving user notifications and deleting specific notifications.

**EventController**

Handles event-related operations like adding new events, retrieving events published by users, retrieving all events, retrieving a specific event, deleting events, 
updating events, searching events by name, marking events as done, and retrieving popular events.

**CommentController**

Manages comment-related operations including saving comments for events, deleting comments, retrieving all comments for an event, updating comments, 
liking comments, and disliking comments.

**AdminController**

Handles administrative operations like adding new users, retrieving all users, and updating existing users. 
Requires admin privileges for access.

**AuthenticationController**

Manages authentication-related operations such as user registration, user authentication, and user logout. 
Utilizes JWT tokens for authentication.

**Authentication:**

JWT tokens are used for authentication throughout the system.
Endpoints requiring authentication are protected, and users must provide a valid JWT token to access them.
The system provides endpoints for user registration, authentication, and logout.

**Security Configuration:**

The system's security configuration ensures that endpoints are properly secured based on their functionalities.
Different roles and authorities are defined to control access to various endpoints.
CORS is configured to allow cross-origin requests from all origins.

**Docker Support:**

This project contains Docker support for easy deployment and scalability.
Docker containers can be used to package the application and its dependencies for deployment in any environment.

**Testing:**

The project includes comprehensive testing to ensure the reliability and correctness of its functionalities.

**Frontend**

Additionally, this project includes a frontend made in React, TypeScript, and Bootstrap. 
The frontend provides a user-friendly interface for interacting with the Event Ticketing System.

**Database**

This project utilizes Spring Data JPA/Hibernate to communicate with a MySQL database. 
This setup enables efficient storage and retrieval of data, ensuring seamless interaction between the application and the database.