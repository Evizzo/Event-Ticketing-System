package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository usersRepository;
    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPurchaseTicket_Success() {
        // Mocking necessary dependencies
        Event event = new Event();
        event.setTicketPrice(BigDecimal.valueOf(50.00));
        event.setCapacity(100);
        when(eventRepository.findById(UUID.fromString("74b3a465-c5a7-4850-a590-80db8db84b0f"))).thenReturn(Optional.of(event));

        User user = new User();
        user.setCredits(BigDecimal.valueOf(100.00));
        when(usersRepository.findById(UUID.fromString("c65d8e0d-6bc9-4a1b-a981-0ef9c74d2da4"))).thenReturn(Optional.of(user));

        when(ticketRepository.save(any())).thenReturn(new Ticket());

        // Test purchaseTicket method
        Optional<Ticket> ticketOptional = ticketService.purchaseTicket(UUID.fromString("74b3a465-c5a7-4850-a590-80db8db84b0f"), UUID.fromString("c65d8e0d-6bc9-4a1b-a981-0ef9c74d2da4"));

        // Verify successful purchase
        assertTrue(ticketOptional.isPresent());
        String responseMessage = "Ticket purchased successfully. Ticket ID: " + ticketOptional.get().getId();
        ResponseEntity<String> response = ResponseEntity.ok(responseMessage);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
    }

    @Test
    public void testPurchaseTicket_InvalidEvent() {
        // Mocking the repository to return null (invalid event)
        when(eventRepository.findById(UUID.fromString("74b3a465-c5a7-4850-a590-80db8db84b0f"))).thenReturn(Optional.empty());

        // Test purchaseTicket method with an invalid event
        Optional<Ticket> ticketOptional = ticketService.purchaseTicket(UUID.fromString("74b3a465-c5a7-4850-a590-80db8db84b0f"), UUID.fromString("c65d8e0d-6bc9-4a1b-a981-0ef9c74d2da4"));

        // Verify that an empty Optional is returned
        assertTrue(ticketOptional.isEmpty());

        // Construct a bad request response
        String errorMessage = "Invalid event.";
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

}