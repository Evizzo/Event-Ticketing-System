package com.eventticketingsystem.eventticketingsystem.services;

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
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EventService {
    private final NotificationService notificationService;
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
                notificationService.sendEventNotification("Event ended.",event.getName() + " is done", eventId);
                return eventRepository.save(event);
            }
            else{
                event.setDone(false);
                notificationService.sendEventNotification("Event started.",event.getName() + " started", eventId);
                return eventRepository.save(event);
            }
        } else {
            throw new RuntimeException("You are not authorized to mark this event as done.");
        }
    }

    public List<Event> getEventsByPublisherId(String sortCriteria, HttpServletRequest request) {
        return switch (sortCriteria) {
            case "date" -> eventRepository.findByPublisherIdOrderByDateAsc(jwtService.extractUserIdFromToken(request));
            case "price" -> eventRepository.findByPublisherIdOrderByTicketPriceAsc(jwtService.extractUserIdFromToken(request));
            default -> eventRepository.findByPublisherIdOrderByNameAsc(jwtService.extractUserIdFromToken(request));
        };
    }
    public List<Event> findAllEvents(String sortCriteria){
        return switch (sortCriteria) {
            case "date" -> eventRepository.findAllByOrderByDateAsc();
            case "price" -> eventRepository.findAllByOrderByTicketPriceAsc();
            default -> eventRepository.findAllByOrderByNameAsc();
        };
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
                notificationService.sendEventNotification("Event canceled.", event.getName() + " is canceled, your money has been refunded.", id);
                ticketService.refundUsersForCanceledEvent(id);
                eventRepository.deleteById(id);
            } else {
                throw new RuntimeException("You are not authorized to delete this event.");
            }
        });
    }

    public Optional<Event> updateEvent(UUID id, Event updatedEvent, HttpServletRequest request) {
        UUID userIdFromToken = jwtService.extractUserIdFromToken(request);

        return eventRepository.findById(id)
                .map(existingEvent -> {
                    User publisher = existingEvent.getPublisher();
                    if (publisher == null) {
                        throw new RuntimeException("Publisher information is missing for this event.");
                    }

                    UUID publisherId = publisher.getId();
                    if (userIdFromToken.equals(publisherId)) {
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
                        notificationService.sendEventNotification("Event updated.", existingEvent.getName() + " is updated.", id);
                        return Optional.of(updated);
                    } else {
                        throw new RuntimeException("You are not authorized to update this event.");
                    }
                })
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }
    public List<Event> findTop3MostPopularEvents() {
        return eventRepository.findMostPopularEventsSortedByName(PageRequest.of(0, 3));
    }
}
