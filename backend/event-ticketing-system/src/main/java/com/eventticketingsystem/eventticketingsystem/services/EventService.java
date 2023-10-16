package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.database.Event;
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
                    if (updatedEvent.getName() != null) {
                        existingEvent.setName(updatedEvent.getName());
                    }
                    if (updatedEvent.getDate() != null) {
                        existingEvent.setDate(updatedEvent.getDate());
                    }
                    if (updatedEvent.getLocation() != null) {
                        existingEvent.setLocation(updatedEvent.getLocation());
                    }
                    if (updatedEvent.getDescription() != null) {
                        existingEvent.setDescription(updatedEvent.getDescription());
                    }
                    if (updatedEvent.getCapacity() != 0) {
                        existingEvent.setCapacity(updatedEvent.getCapacity());
                    }
                    if (updatedEvent.getTicketPrice() != null) {
                        existingEvent.setTicketPrice(updatedEvent.getTicketPrice());
                    }

                    Event updated = eventRepository.save(existingEvent);
                    return Optional.of(updated);
                })
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }
}
