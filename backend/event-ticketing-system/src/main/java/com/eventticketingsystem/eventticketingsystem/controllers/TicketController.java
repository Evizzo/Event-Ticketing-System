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

@RestController
@AllArgsConstructor
@RequestMapping("event")
public class TicketController {
    private final TicketService ticketService;
    private final JwtService jwtService;
    @PostMapping("/{eventId}/ticket")
    public ResponseEntity<String> purchaseTicket(@PathVariable UUID eventId, HttpServletRequest request) {
        return ticketService.purchaseTicket(eventId, jwtService.extractUserIdFromToken(request))
                .map(purchasedTicket -> ResponseEntity.ok("Ticket purchased successfully. Ticket ID: " + purchasedTicket.getId()))
                .orElseThrow(() -> new TicketNotFoundException("Ticket purchasing failed."));
    }
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
