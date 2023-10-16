package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.database.Event;
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
public class EventController {
    private final EventService eventService;
    @PostMapping("event")
    public ResponseEntity<Event> addNewEvent(@Valid @RequestBody Event event){
        return ResponseEntity.ok(eventService.saveEvent(event));
    }
    @GetMapping("event")
    public ResponseEntity<List<Event>> retriveAllEvents(){
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    @GetMapping("event/{id}")
    public ResponseEntity<Event> retriveEvent(@PathVariable UUID id){
        return eventService.findEventById(id)
                .map(event -> ResponseEntity.ok(event))
                .orElseThrow(() -> new EventNotFoundException("id: " + id));
    }
    @DeleteMapping("event/{id}")
    @Transactional
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id) {
        return eventService.findEventById(id)
                .map(event -> {
                    eventService.deleteEventById(id);
                    return ResponseEntity.ok("Event deleted successfully.");
                })
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }
    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @Valid @RequestBody Event event) {
        return eventService.updateEvent(id, event)
                .map(updatedEvent -> ResponseEntity.ok(updatedEvent))
                .orElseThrow(() -> new EventNotFoundException("id: " + id));
    }
}
