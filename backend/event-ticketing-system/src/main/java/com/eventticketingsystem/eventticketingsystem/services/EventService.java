package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.auth.AuthenticationService;
import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.EventNotFoundException;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EventService {
    private final TicketService ticketService;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final JwtService jwtService;
    public List<Event> findEventsByName(String name) {
        return eventRepository.findByNameContainingIgnoreCase(name);
    }
    public Event saveEvent(Event event, HttpServletRequest request){
        UUID userId = jwtService.extractUserIdFromToken(request);
        User user = userService.findUserById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        event.setPublisher(user);
        event.setDone(false);
        return eventRepository.save(event);
    }
    public Event eventIsDone(UUID eventId, HttpServletRequest request) {
        UUID userIdFromToken = jwtService.extractUserIdFromToken(request);

        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        User publisher = event.getPublisher();
        if (publisher == null) {
            throw new RuntimeException("Publisher information is missing for this event.");
        }

        UUID publisherId = publisher.getId();
        if (userIdFromToken.equals(publisherId)) {
            if(!event.isDone()){
                event.setDone(true);
                return eventRepository.save(event);
            }
            else{
                event.setDone(false);
                return eventRepository.save(event);
            }
        } else {
            throw new RuntimeException("You are not authorized to mark this event as done.");
        }
    }

    public List<Event> getEventsByPublisherId(HttpServletRequest request) {
        return eventRepository.findByPublisherId(jwtService.extractUserIdFromToken(request));
    }
    public List<Event> findAllEvents(){
        return eventRepository.findAll();
    }
    public Optional<Event> findEventById(UUID id){
        return eventRepository.findById(id);
    }
    public void deleteEventById(UUID id, HttpServletRequest request) {
        UUID publisherIdFromToken = jwtService.extractUserIdFromToken(request);
        Optional<Event> optionalEvent = eventRepository.findById(id);

        optionalEvent.ifPresent(event -> {
            UUID eventPublisherId = event.getPublisher().getId();
            if (eventPublisherId.equals(publisherIdFromToken)) {
                ticketService.refundUsersForCanceledEvent(id);
                eventRepository.deleteById(id);
            } else {
                throw new RuntimeException("You are not authorized to delete this event.");
            }
        });
    }

    public Optional<Event> updateEvent(UUID id, Event updatedEvent) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    Optional.ofNullable(updatedEvent.getName()).ifPresent(existingEvent::setName);
                    Optional.ofNullable(updatedEvent.getDate()).ifPresent(existingEvent::setDate);
                    Optional.ofNullable(updatedEvent.getLocation()).ifPresent(existingEvent::setLocation);
                    Optional.ofNullable(updatedEvent.getDescription()).ifPresent(existingEvent::setDescription);
                    Optional.ofNullable(updatedEvent.getCapacity()).ifPresent(existingEvent::setCapacity);
                    Optional.ofNullable(updatedEvent.getTicketPrice()).ifPresent(existingEvent::setTicketPrice);

                    Event updated = eventRepository.save(existingEvent);

                    List<Ticket> eventTickets = ticketRepository.findTicketsByEventId(updatedEvent.getId());
                    for (Ticket ticket : eventTickets) {
                        ticket.setEvent(updated);
                        ticketRepository.save(ticket);
                    }

                    return Optional.of(updated);
                })
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }
}
