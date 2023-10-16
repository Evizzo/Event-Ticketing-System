package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.database.Event;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindEventById() {
        // Mocking the repository to return an event with ID 1
        Event event = new Event();
        event.setId(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"));
        when(eventRepository.findById(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"))).thenReturn(Optional.of(event));

        Optional<Event> foundEvent = eventService.findEventById(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"));

        // Verify that the event is found and returned correctly
        assertEquals(event, foundEvent.orElse(null));
    }

    @Test
    public void testDeleteEventById() {
        // Mocking the repository to return an event with ID 1
        Event event = new Event();
        event.setId(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"));
        when(eventRepository.findById(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"))).thenReturn(Optional.of(event));

        eventService.deleteEventById(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"));

        // Verify that deleteEventById deletes the event and associated tickets
        verify(eventRepository, times(1)).deleteById(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"));
        verify(ticketRepository, times(1)).deleteByEventId(UUID.fromString("8a9b6ac4-e3e0-4824-a543-6c89a1e31c2e"));
    }

    @Test
    public void testSaveEvent() {
        Event event = new Event();
        event.setName("Test Event");

        eventService.saveEvent(event);

        // Verify that the event is saved
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testFindAllEvents() {
        // Mocking the repository to return a list of events
        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event());
        eventList.add(new Event());
        when(eventRepository.findAll()).thenReturn(eventList);

        List<Event> foundEvents = eventService.findAllEvents();

        // Verify that the list of events is returned correctly
        assertEquals(eventList, foundEvents);
    }
}