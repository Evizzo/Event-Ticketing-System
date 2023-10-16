package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.exceptions.TicketNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    @PostMapping("/event/{eventId}/ticket")
    public ResponseEntity<String> purchaseTicket(@PathVariable UUID eventId, @RequestParam UUID userId) {
        // link example: http://localhost:8080/event/25fc8ca7-7ad9-438d-908a-0d8324361068/ticket?userId=2
        return ticketService.purchaseTicket(eventId, userId)
                .map(purchasedTicket -> ResponseEntity.ok("Ticket purchased successfully. Ticket ID: " + purchasedTicket.getId()))
                .orElseThrow(() -> new TicketNotFoundException("Ticket purchasing failed."));
    }
    @DeleteMapping("/event/refund/{ticketId}")
    public ResponseEntity<String> refundTicket(
            @PathVariable UUID ticketId,
            @RequestParam UUID userId,
            @RequestParam int refundAmount
    ) {
//  http://localhost:8080/event/refund/896193e1-3632-4695-bd9b-16d6ee9905cc?userId=a1a2b6da-aa65-4f81-88e9-f2d36d7e0e6a&refundAmount=2
    return ticketService.findTicketById(ticketId)
            .map(ticket -> {
                ticketService.refundTicket(ticketId, userId, refundAmount);
                return ResponseEntity.ok("Ticket refunded successfully.");
            })
            .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + ticketId));
    }
}
