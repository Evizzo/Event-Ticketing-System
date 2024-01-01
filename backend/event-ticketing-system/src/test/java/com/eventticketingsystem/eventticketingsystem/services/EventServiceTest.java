package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.EventNotFoundException;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findEventsByName_WhenValidName_ReturnsEvents() {
        String eventName = "Test Event";
        List<Event> mockEvents = Arrays.asList(new Event(), new Event());

        when(eventRepository.findByNameContainingIgnoreCase(eventName)).thenReturn(mockEvents);

        List<Event> foundEvents = eventService.findEventsByName(eventName);

        assertEquals(2, foundEvents.size());
        verify(eventRepository, times(1)).findByNameContainingIgnoreCase(eventName);
    }

    @Test
    void saveEvent_WhenValidEventAndUser_ReturnsSavedEvent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Event event = new Event();
        User user = new User();
        user.setId(UUID.randomUUID());

        when(jwtService.extractUserIdFromToken(request)).thenReturn(user.getId());
        when(userService.findUserById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event savedEvent = eventService.saveEvent(event, request);

        assertNotNull(savedEvent);
        assertEquals(user, savedEvent.getPublisher());
        assertFalse(savedEvent.isDone());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void saveEvent_WhenUserNotFound_ThrowsUserNotFoundException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Event event = new Event();
        UUID userId = UUID.randomUUID();

        when(jwtService.extractUserIdFromToken(request)).thenReturn(userId);
        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> eventService.saveEvent(event, request));
    }

    @Test
    void eventIsDone_WhenEventFoundAndUserIsNotPublisher_ThrowsRuntimeException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Event event = new Event();
        event.setId(eventId);

        User publisher = new User();
        publisher.setId(UUID.randomUUID());

        when(jwtService.extractUserIdFromToken(request)).thenReturn(userId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(RuntimeException.class, () -> eventService.eventIsDone(eventId, request));
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void getEventsByPublisherId_ReturnsEventsForPublisher() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID userId = UUID.randomUUID();
        List<Event> mockEvents = Arrays.asList(new Event(), new Event());

        when(jwtService.extractUserIdFromToken(request)).thenReturn(userId);
        when(eventRepository.findByPublisherId(userId)).thenReturn(mockEvents);

        List<Event> events = eventService.getEventsByPublisherId("name",request);

        assertEquals(2, events.size());
        verify(eventRepository, times(1)).findByPublisherId(userId);
    }

    @Test
    void findAllEvents_ReturnsAllEvents() {
        List<Event> mockEvents = Arrays.asList(new Event(), new Event());

        when(eventRepository.findAll()).thenReturn(mockEvents);

        List<Event> events = eventService.findAllEvents("name");

        assertEquals(2, events.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void findEventById_WhenValidId_ReturnsEvent() {
        UUID eventId = UUID.randomUUID();
        Event mockEvent = new Event();
        mockEvent.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));

        Optional<Event> event = eventService.findEventById(eventId);

        assertTrue(event.isPresent());
        assertEquals(eventId, event.get().getId());
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void findEventById_WhenInvalidId_ReturnsEmptyOptional() {
        UUID invalidId = UUID.randomUUID();

        when(eventRepository.findById(invalidId)).thenReturn(Optional.empty());

        Optional<Event> event = eventService.findEventById(invalidId);

        assertTrue(event.isEmpty());
        verify(eventRepository, times(1)).findById(invalidId);
    }

    @Test
    void deleteEventById_WhenValidIdAndUserIsPublisher_DeletesEvent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Event event = new Event();
        event.setId(eventId);
        User publisher = new User();
        publisher.setId(userId);
        event.setPublisher(publisher);

        when(jwtService.extractUserIdFromToken(request)).thenReturn(userId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.deleteEventById(eventId, request);

        verify(ticketService, times(1)).refundUsersForCanceledEvent(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void deleteEventById_WhenValidIdAndUserIsNotPublisher_ThrowsRuntimeException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Event event = new Event();
        event.setId(eventId);
        User publisher = new User();
        publisher.setId(UUID.randomUUID());
        event.setPublisher(publisher);

        when(jwtService.extractUserIdFromToken(request)).thenReturn(userId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(RuntimeException.class, () -> eventService.deleteEventById(eventId, request));
        verify(ticketService, never()).refundUsersForCanceledEvent(eventId);
        verify(eventRepository, never()).deleteById(eventId);
    }

    @Test
    void updateEvent_WhenValidIdAndEventExistsAndUserIsPublisher_UpdatesEvent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setName("Old Event Name");

        Event updatedEvent = new Event();
        updatedEvent.setId(eventId);
        updatedEvent.setName("Updated Event Name");

        User publisher = new User();
        publisher.setId(userId);
        existingEvent.setPublisher(publisher);

        when(jwtService.extractUserIdFromToken(request)).thenReturn(userId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);
        when(ticketRepository.findTicketsByEventId(eventId)).thenReturn(Collections.emptyList());

        Optional<Event> result = eventService.updateEvent(eventId, updatedEvent, request);

        assertTrue(result.isPresent());
        assertEquals("Updated Event Name", result.get().getName());
        verify(eventRepository, times(1)).save(existingEvent);
        verify(ticketRepository, times(1)).findTicketsByEventId(eventId);
    }

    @Test
    void updateEvent_WhenValidIdAndEventExistsAndUserIsNotPublisher_ThrowsRuntimeException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setName("Old Event Name");

        Event updatedEvent = new Event();
        updatedEvent.setId(eventId);
        updatedEvent.setName("Updated Event Name");

        User publisher = new User();
        publisher.setId(UUID.randomUUID());
        existingEvent.setPublisher(publisher);

        when(jwtService.extractUserIdFromToken(request)).thenReturn(userId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));

        assertThrows(RuntimeException.class, () -> eventService.updateEvent(eventId, updatedEvent, request));
        verify(eventRepository, never()).save(any(Event.class));
        verify(ticketRepository, never()).findTicketsByEventId(eventId);
    }

    @Test
    void updateEvent_WhenInvalidEventId_ThrowsEventNotFoundException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID invalidId = UUID.randomUUID();
        Event updatedEvent = new Event();

        when(eventRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(invalidId, updatedEvent, request));
        verify(eventRepository, never()).save(any(Event.class));
    }

}
