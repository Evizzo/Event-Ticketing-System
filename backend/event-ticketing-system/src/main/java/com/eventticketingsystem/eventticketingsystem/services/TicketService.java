package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.entities.TicketStatus;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RedeemCodeService redeemCodeService;
    public Optional<Ticket> purchaseTicket(UUID eventId, UUID userId, String codeName) {
        return eventRepository.findById(eventId)
                .flatMap(event -> userRepository.findById(userId)
                        .map(user -> {
                            if(event.isDone()){
                                return Optional.<Ticket>empty();
                            }

                            BigDecimal ticketPrice = event.getTicketPrice();

                            if (codeName != null && !codeName.isEmpty()) {
                                ticketPrice = redeemCodeService.calculatePriceWithCode(ticketPrice, codeName);
                            }

                            BigDecimal lambdaTicketPrice = ticketPrice;
                            Optional<Ticket> existingTicket = user.getTickets().stream()
                                    .filter(ticket -> ticket.getEvent().getId().equals(eventId)
                                            && ticket.getPaidAmount().equals(lambdaTicketPrice))
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
                                ticket.setPaidAmount(ticketPrice);
                                user.getTickets().add(ticket);
                            }

                            event.setCapacity(event.getCapacity() - 1);

                            BigDecimal newCredits = user.getCredits().subtract(ticketPrice);

                            int comparison = newCredits.compareTo(BigDecimal.ZERO);
                            if (comparison < 0) {
                                throw new RuntimeException("You don't have enough credits.");
                            }

                            user.setCredits(newCredits);

                            ticketRepository.saveAll(user.getTickets());
                            eventRepository.save(event);

                            return Optional.of(existingTicket.orElseGet(() -> user.getTickets().get(user.getTickets().size() - 1)));
                        }))
                .orElse(Optional.empty());
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
                throw new RuntimeException("Invalid amount to refund");
            }

            BigDecimal refundPrice = ticket.getPaidAmount().multiply(BigDecimal.valueOf(refundAmount));

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
    public void refundUsersForCanceledEvent(UUID eventId) {
        List<Ticket> tickets = ticketRepository.findTicketsByEventId(eventId);
        for (Ticket ticket : tickets) {
            refundTicket(ticket.getId(), ticket.getUser().getId(), ticket.getAmount());
            ticketRepository.delete(ticket);
        }
    }
    public Optional<Ticket> findUserTicketByEventId(UUID userId, UUID eventId) {
        return userRepository.findById(userId)
                .flatMap(user -> user.getTickets().stream()
                        .filter(ticket -> ticket.getEvent().getId().equals(eventId))
                        .findFirst());
    }
}
