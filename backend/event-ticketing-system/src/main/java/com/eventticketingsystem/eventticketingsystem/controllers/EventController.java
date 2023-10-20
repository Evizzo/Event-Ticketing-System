package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.exceptions.EventNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.EventService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("event")
public class EventController {
    private final EventService eventService;
    public static final String EVENT_NOT_FOUND = "Event not found with ID: ";
    @PostMapping
    public ResponseEntity<Event> addNewEvent(@Valid @RequestBody Event event){
        return ResponseEntity.ok(eventService.saveEvent(event));
    }
    @GetMapping
    public ResponseEntity<List<Event>> retrieveAllEvents(){
        return ResponseEntity.ok(eventService.findAllEvents());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Event> retrieveEvent(@PathVariable UUID id){
        return eventService.findEventById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EventNotFoundException(EVENT_NOT_FOUND + id));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id) {
        return eventService.findEventById(id)
                .map(event -> {
                    eventService.deleteEventById(id);
                    return ResponseEntity.ok("Event deleted successfully.");
                })
                .orElseThrow(() -> new EventNotFoundException(EVENT_NOT_FOUND + id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @Valid @RequestBody Event event) {
        return eventService.updateEvent(id, event)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EventNotFoundException(EVENT_NOT_FOUND + id));
    }
}
