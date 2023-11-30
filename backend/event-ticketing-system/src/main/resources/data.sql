INSERT INTO users (id, firstname, lastname, email, password, credits, role)
VALUES
  (UNHEX('a4a8456f23ce4f029b6913bbdd74a045'), 'John', 'Doe', 'johndoe@example.com', 'hashed_password_1', 1000.00, 'USER'),
  (UNHEX('a1a2b6daaa654f8188e9f2d36d7e0e6a'), 'Jane', 'Smith', 'janesmith@example.com', 'hashed_password_2', 5000.00, 'USER');

INSERT INTO event (id, name, date, location, description, capacity, ticket_price)
VALUES
  (UNHEX('8a9b6ac4e3e04824a5436c89a1e31c2e'), 'Concert 1', '2024-10-15', 'Venue A', 'Concert description 1', 500, 30.00),
  (UNHEX('321b6ac4e3e04824a5436c89a1e31c2e'), 'TEST 1', '2024-10-15', 'TEST A', 'TEST description 1', 400, 40.00),
  (UNHEX('6f885b26f61b41b0a83f390bfb4be11a'), 'Conference', '2024-11-02', 'Venue B', 'Conference description', 200, 50.00);