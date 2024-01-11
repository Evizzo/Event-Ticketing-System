package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.Notification;
import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.EventNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.NotificationRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    public void sendNotification(String title, String message, UUID eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        List<Ticket> eventTickets = ticketRepository.findTicketsByEventId(eventId);

        Set<UUID> notifiedUsers = new HashSet<>();

        for (Ticket ticket : eventTickets) {
            User user = ticket.getUser();
            UUID userId = user.getId();

            if (!notifiedUsers.contains(userId)) {
                Notification notification = new Notification();
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setCreatedAt(LocalDateTime.now());
                notification.setUser(user);
                notificationRepository.save(notification);

                notifiedUsers.add(userId);
            }
        }
    }
    public List<Notification> retrieveAllUserNotifications(UUID id){
        return notificationRepository.findAllByUser_IdOrderByCreatedAtDesc(id);
    }
    public Optional<Notification> findNotificationById(UUID id){
        return notificationRepository.findById(id);
    }
    public void deleteNotificationById(UUID id){
        notificationRepository.deleteById(id);
    }

    public void sendWelcomeNotification(User user) {
        Notification welcomeNotification = new Notification();
        welcomeNotification.setTitle("Welcome!");
        welcomeNotification.setMessage("Welcome to our platform!");
        welcomeNotification.setCreatedAt(LocalDateTime.now());
        welcomeNotification.setUser(user);
        notificationRepository.save(welcomeNotification);
    }
}
