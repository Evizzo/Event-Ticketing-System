INSERT INTO users (id, firstname, lastname, email, password, credits)
VALUES
  ('a4a8456f-23ce-4f02-9b69-13bbdd74a045', 'John', 'Doe', 'johndoe@example.com', 'hashed_password_1', 1000.00),
  ('a1a2b6da-aa65-4f81-88e9-f2d36d7e0e6a', 'Jane', 'Smith', 'janesmith@example.com', 'hashed_password_2', 5000.00);

INSERT INTO event (id, name, date, location, description, capacity, ticket_price)
VALUES
  ('8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e', 'Concert 1', '2024-10-15', 'Venue A', 'Concert description 1', 500, 30.00),
  ('321b6ac4-e3e0-4824-a543-6c89a1e31c2e', 'TEST 1', '2024-10-15', 'TEST A', 'TEST description 1', 400, 40.00),
  ('6f885b26-f61b-41b0-a83f-390bfb4be11a', 'Conference', '2023-11-02', 'Venue B', 'Conference description', 200, 50.00);

--INSERT INTO ticket (id, event_id, user_id, purchase_date, status)
--VALUES
--  ('74b3a465-c5a7-4850-a590-80db8db84b0f', '8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e', 'a4a8456f-23ce-4f02-9b69-13bbdd74a045', '2023-10-10 10:00:00', 0),
--  ('c65d8e0d-6bc9-4a1b-a981-0ef9c74d2da4', '6f885b26-f61b-41b0-a83f-390bfb4be11a', 'a1a2b6da-aa65-4f81-88e9-f2d36d7e0e6a', '2023-11-01 15:30:00', 0);
