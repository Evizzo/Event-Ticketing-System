package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.exceptions.TicketNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
/**
 * Controller class for managing ticket-related operations.
 */
@RestController
@AllArgsConstructor
@RequestMapping("event")
public class TicketController {
    private final TicketService ticketService;
    private final JwtService jwtService;
    /**
     * Purchases a ticket for the specified event.
     *
     * @param eventId The ID of the event for which the ticket is being purchased.
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @return ResponseEntity indicating the success of the ticket purchase.
     * @throws TicketNotFoundException if the ticket is not found.
     */
    @PostMapping("/{eventId}/ticket")
    public ResponseEntity<String> purchaseTicket(@PathVariable UUID eventId, HttpServletRequest request) {
        return ticketService.purchaseTicket(eventId, jwtService.extractUserIdFromToken(request))
                .map(purchasedTicket -> ResponseEntity.ok("Ticket purchased successfully. Ticket ID: " + purchasedTicket.getId()))
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found."));
    }
    /**
     * Refunds a ticket for the specified ticket ID.
     *
     * @param ticketId The ID of the ticket to be refunded.
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @param refundAmount The amount to be refunded for the ticket.
     * @return ResponseEntity indicating the success of the ticket refund.
     * @throws TicketNotFoundException if the ticket is not found.
     */
    @DeleteMapping("/refund/{ticketId}")
    public ResponseEntity<String> refundTicket(
            @PathVariable UUID ticketId,
            HttpServletRequest request,
            @RequestParam int refundAmount
    ) {
    return ticketService.findTicketById(ticketId)
            .map(ticket -> {
                ticketService.refundTicket(ticketId, jwtService.extractUserIdFromToken(request), refundAmount);
                return ResponseEntity.ok("Ticket refunded successfully.");
            })
            .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));
    }
    /**
     * Retrieves a user's ticket for the specified event ID.
     *
     * @param eventId The ID of the event for which the ticket is being retrieved.
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @return ResponseEntity containing the user's ticket for the specified event.
     * @throws TicketNotFoundException if the ticket is not found.
     */
    @GetMapping("/ticketId/{eventId}")
    public ResponseEntity<Ticket> getUserTicketByEventId(
            @PathVariable UUID eventId,
            HttpServletRequest request
    ) {
        return ticketService.findUserTicketByEventId(jwtService.extractUserIdFromToken(request), eventId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found for user ID: " + jwtService.extractUserIdFromToken(request)
                        + " and event ID: " + eventId));
    }
}
