package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.exceptions.EventNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    public Event saveEvent(Event event){
        return eventRepository.save(event);
    }
    public List<Event> findAllEvents(){
        return eventRepository.findAll();
    }
    public Optional<Event> findEventById(UUID id){
        return eventRepository.findById(id);
    }
    public void deleteEventById(UUID id){
        ticketRepository.deleteByEventId(id);
        eventRepository.deleteById(id);
    }
    public Optional<Event> updateEvent(UUID id, Event updatedEvent) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    Optional.ofNullable(updatedEvent.getName()).ifPresent(existingEvent::setName);
                    Optional.ofNullable(updatedEvent.getDate()).ifPresent(existingEvent::setDate);
                    Optional.ofNullable(updatedEvent.getLocation()).ifPresent(existingEvent::setLocation);
                    Optional.ofNullable(updatedEvent.getDescription()).ifPresent(existingEvent::setDescription);
                    Optional.of(updatedEvent.getCapacity()).ifPresent(existingEvent::setCapacity);
                    Optional.ofNullable(updatedEvent.getTicketPrice()).ifPresent(existingEvent::setTicketPrice);

                    Event updated = eventRepository.save(existingEvent);

                    return Optional.of(updated);
                })
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }
}
