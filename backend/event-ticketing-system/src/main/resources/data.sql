INSERT INTO users (id, firstname, lastname, email, password, credits, role)
VALUES
 (UNHEX('3f883b26f61b41b0a83f390bfb4b311a'), 'Stefan', 'Stefanovic', 'stefan@gmail.com', '$2a$10$.pPphHcTYF5iSV/rIMdF0OtfJ0.gJMhCHdTFD3sxCG88LXF8mugim', 2000, 'USER'),
 (UNHEX('2f883b26f61b41b0a83f390bfb4b311a'), 'Ema', 'Emic', 'ema@gmail.com', '$2a$10$h3Jn0SC8MPGszKYp94aaDO09XjUqyk4VdsIfaa9su/SVjuT.IIE2S', 1840, 'USER');

INSERT INTO event (id, name, date, location, description, capacity, ticket_price, publisher_id, is_done)
VALUES
  (UNHEX('8a9b6ac4e3e04824a5436c89a1e31c2e'), 'Concert 1', '2027-10-15', 'Venue A', 'Concert description 1', 500, 30.00, UNHEX('3f883b26f61b41b0a83f390bfb4b311a'), false),
  (UNHEX('6f885b26f61b41b0a83f390bfb4be11a'), 'Conference', '2027-11-02', 'Venue B', 'Conference description', 200, 50.00, UNHEX('3f883b26f61b41b0a83f390bfb4b311a'), false),
  (UNHEX('6f825b26f61b41b0a83f390bfb4be11a'), 'Event', '2027-11-07', 'Venue C', 'Event description', 300, 70.00, UNHEX('3f883b26f61b41b0a83f390bfb4b311a'), false);

INSERT INTO ticket (id, event_id, user_id, purchase_date, status, amount, paid_amount)
VALUES
    (UNHEX('8a9b6ac4e3e04824a5476c89a1e31c2e'), UNHEX('8a9b6ac4e3e04824a5436c89a1e31c2e'), UNHEX('2f883b26f61b41b0a83f390bfb4b311a'), '2025-11-02', 1, 2, 30.00),
    (UNHEX('8a9b6ac4e3e04824a5469c89a1e31c2e'), UNHEX('6f885b26f61b41b0a83f390bfb4be11a'), UNHEX('2f883b26f61b41b0a83f390bfb4b311a'), '2025-10-01', 1, 2, 50.00);