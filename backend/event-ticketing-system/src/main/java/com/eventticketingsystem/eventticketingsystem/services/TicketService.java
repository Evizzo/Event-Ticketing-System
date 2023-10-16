package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.database.Event;
import com.eventticketingsystem.eventticketingsystem.database.Ticket;
import com.eventticketingsystem.eventticketingsystem.database.TicketStatus;
import com.eventticketingsystem.eventticketingsystem.database.User;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EventService eventService;
    public Optional<Ticket> purchaseTicket(UUID eventId, UUID userId) {
        try {
            Event event = eventRepository.findById(eventId).orElse(null);

            if (event == null) {
                return Optional.empty();
            }

            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                return Optional.empty();
            }

            Optional<Ticket> existingTicket = user.getTickets().stream()
                    .filter(ticket -> ticket.getEvent().getId().equals(eventId))
                    .findFirst();

            if (existingTicket.isPresent()) {
                Ticket ticket = existingTicket.get();
                ticket.setAmount(ticket.getAmount() + 1);
            } else {
                Ticket ticket = new Ticket();
                ticket.setEvent(event);
                ticket.setUser(user);
                ticket.setPurchaseDate(LocalDateTime.now());
                ticket.setStatus(TicketStatus.PURCHASED);
                ticket.setAmount(1);
                user.getTickets().add(ticket);
            }

            event.setCapacity(event.getCapacity() - 1);

            BigDecimal newCredits = user.getCredits().subtract(event.getTicketPrice());

            int comparison = newCredits.compareTo(BigDecimal.ZERO);
            if (comparison < 0) {
                return Optional.empty();
            }

            user.setCredits(newCredits);

            ticketRepository.saveAll(user.getTickets());
            eventRepository.save(event);

            return Optional.of(existingTicket.isPresent() ? existingTicket.get() : user.getTickets().get(user.getTickets().size() - 1));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Ticket> findTicketById(UUID ticketId){
        return ticketRepository.findById(ticketId);
    }

    public void deleteTicketById(UUID ticketId){
        ticketRepository.deleteById(ticketId);
    }

    public void refundTicket(UUID ticketId, UUID userId, int refundAmount) {
        Optional<Ticket> optionalTicket = findTicketById(ticketId);
        Optional<User> optionalUser = userService.findUserById(userId);

        if (optionalTicket.isPresent() && optionalUser.isPresent()) {
            Ticket ticket = optionalTicket.get();
            User user = optionalUser.get();
            Event event = ticket.getEvent();

            int currentAmount = ticket.getAmount();
            if (refundAmount <= 0 || refundAmount > currentAmount) {
                throw new RuntimeException("Invalid amout to refound");
            }

            BigDecimal refundPrice = event.getTicketPrice().multiply(BigDecimal.valueOf(refundAmount));

            user.setCredits(user.getCredits().add(refundPrice));
            ticket.setAmount(currentAmount - refundAmount);

            event.setCapacity(event.getCapacity() + refundAmount);

            if (ticket.getAmount() == 0) {
                deleteTicketById(ticketId);
            }
            else{
                ticketRepository.save(ticket);
            }
            eventRepository.save(event);
            userRepository.save(user);
        }
    }
}
