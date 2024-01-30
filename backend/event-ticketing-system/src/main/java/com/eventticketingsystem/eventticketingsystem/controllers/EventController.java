package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.exceptions.EventNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * Controller class for managing event-related operations.
 */
@AllArgsConstructor
@RestController
@RequestMapping("event")
public class EventController {
    private final EventService eventService;
    public static final String EVENT_NOT_FOUND = "Event not found with ID: ";
    /**
     * Adds a new event to the system.
     *
     * @param event   The event to be added (validated using @Valid annotation).
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the added Event.
     */
    @PostMapping
    public ResponseEntity<Event> addNewEvent(@Valid @RequestBody Event event, HttpServletRequest request){
        return ResponseEntity.ok(eventService.saveEvent(event, request));
    }
    /**
     * Retrieves all events published by the current logged-in user.
     *
     * @param sortCriteria Sorting criteria for the retrieved events.
     * @param request      HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the list of published events.
     */
    @GetMapping("/published")
    public ResponseEntity<List<Event>> retrieveAllPublishersEvents(@RequestParam String sortCriteria ,HttpServletRequest request){
        return ResponseEntity.ok(eventService.getEventsByPublisherId(sortCriteria, request));
    }
    /**
     * Retrieves all events in the system.
     *
     * @param sortCriteria Sorting criteria for the retrieved events.
     * @return ResponseEntity containing the list of all events.
     */
    @GetMapping
    public ResponseEntity<List<Event>> retrieveAllEvents(@RequestParam String sortCriteria){
        return ResponseEntity.ok(eventService.findAllEvents(sortCriteria));
    }
    /**
     * Retrieves a specific event by its ID.
     *
     * @param id UUID of the event to be retrieved.
     * @return ResponseEntity containing the retrieved Event.
     * @throws EventNotFoundException if the event with the given ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> retrieveEvent(@PathVariable UUID id){
        return eventService.findEventById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EventNotFoundException(EVENT_NOT_FOUND + id));
    }
    /**
     * Deletes a specific event by its ID.
     *
     * @param id      UUID of the event to be deleted.
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity indicating the success of the deletion.
     * @throws EventNotFoundException if the event with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id, HttpServletRequest request) {
        return eventService.findEventById(id)
                .map(event -> {
                    eventService.deleteEventById(id, request);
                    return ResponseEntity.ok("Event deleted successfully.");
                })
                .orElseThrow(() -> new EventNotFoundException(EVENT_NOT_FOUND + id));
    }
    /**
     * Updates a specific event by its ID.
     *
     * @param id      UUID of the event to be updated.
     * @param event   The updated event (validated using @Valid annotation).
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the updated Event.
     * @throws EventNotFoundException if the event with the given ID is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @Valid @RequestBody Event event, HttpServletRequest request) {
        return eventService.updateEvent(id, event, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EventNotFoundException(EVENT_NOT_FOUND + id));
    }
    /**
     * Searches events by name.
     *
     * @param name The name to search for.
     * @return ResponseEntity containing the list of events matching the given name.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEventsByName(@RequestParam String name) {
        return eventService.findEventsByName(name).isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(eventService.findEventsByName(name));
    }
    /**
     * Marks an event as done.
     *
     * @param id      UUID of the event to be marked as done.
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the marked Event.
     */
    @PutMapping("/done/{id}")
    public ResponseEntity<Event> eventIsDone(@PathVariable UUID id, HttpServletRequest request){
        return ResponseEntity.ok(eventService.eventIsDone(id,request));
    }
    /**
     * Retrieves the top 3 most popular events in the system.
     *
     * @return ResponseEntity containing the list of top 3 popular events.
     */
    @GetMapping("/popular")
    public ResponseEntity<List<Event>> retrieveTop3PopularEvents() {
        return ResponseEntity.ok(eventService.findTop3MostPopularEvents());
    }
}
