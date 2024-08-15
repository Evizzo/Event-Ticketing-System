# **Event Ticketing System**

This system provides a platform for managing events, ticket purchases, user interactions, and more. 
Below, you'll find an overview of the controllers in this project along with their functionalities.

# **Tools used**

**Frontend**: HTML, CSS, React, BootStrap.

**Backend**: Java, Spring Boot, Spring Security, Jpa/Hibernate, TypeScript.

**Database**: MySQL.

**Testing**: Mockito, JUnit.

**External APIs**: https://www.exchangerate-api.com/.

**Other**: Docker.

# **Overview:**

**UserController**

GET /user/credits - Retrieve the credits of the currently authenticated user.

GET /user - Retrieve all users.

GET /user/current - Retrieve the details of the currently authenticated user.

DELETE /user/delete-current-user - Delete the currently authenticated user.

PUT /user/update-current-user - Update the details of the currently authenticated user.

GET /user/tickets - Retrieve all tickets associated with the currently authenticated user.

PATCH /user/change-password - Change the password for the currently authenticated user.

GET /user/profile/{email} - Retrieve user information and the list of events they published.

**TicketController**

POST /event/{eventId}/ticket - Purchase a ticket for a specified event, with optional redeem code.

DELETE /event/refund/{ticketId} - Refund a ticket by its ID.

GET /event/ticketId/{eventId} - Retrieve a user's ticket for a specified event.

**NotificationController**

GET /notification - Retrieve all notifications for the current user.

DELETE /notification/delete/{id} - Delete a specific notification by ID.

**EventController**

POST /event - Add a new event.

GET /event/published - Retrieve all events published by the current user.

GET /event - Retrieve all events in the system.

GET /event/{id} - Retrieve a specific event by ID.

DELETE /event/{id} - Delete a specific event by ID.

PUT /event/{id} - Update a specific event by ID.

GET /event/search - Search for events by name.

PUT /event/done/{id} - Mark an event as done.

GET /event/popular - Retrieve the top 3 most popular events.

PUT /event/{id}/like - Like a specific event by ID.

PUT /event/{id}/dislike - Dislike a specific event by ID.

**CurrencyConverterController**

GET /currency-converter - Convert event ticket price from USD to another currency.

**CommentController**

POST /comment/{eventId} - Save a new comment for an event.

DELETE /comment/{id} - Delete a comment by ID.

GET /comment/{eventId} - Retrieve all comments for an event.

PUT /comment/{id} - Update a comment by ID.

PUT /comment/{id}/like - Like a comment by ID.

PUT /comment/{id}/dislike - Dislike a comment by ID.

**AdminController**

POST /admin/user - Add a new user.

GET /admin/users - Retrieve all users.

PUT /admin/user - Update an existing user by ID.

DELETE /admin/user/{id} - Forcefully delete a user by ID.

PUT /admin/redeem/{id} - Edit a redeem code by ID.

GET /admin/redeem-codes - Retrieve all redeem codes.

DELETE /admin/redeem/{id} - Delete a redeem code by ID.

POST /admin/redeem - Add a new redeem code.

**AuthenticationController**

POST /api/v1/auth/register - Register a new user.

POST /api/v1/auth/authenticate - Authenticate a user.

POST /api/v1/auth/logout - Logout the current user.

POST /api/v1/auth/send-email - Send a password reset email.

POST /api/v1/auth/reset-password - Reset password using a token.

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
