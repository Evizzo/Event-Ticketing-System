package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.auth.AuthenticationService;
import com.eventticketingsystem.eventticketingsystem.entities.*;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final AuthenticationService authenticationService;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final TicketService ticketService;

    public User saveUser(User user){
        return userRepository.save(user);
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> updateUser(UUID id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    Optional.ofNullable(updatedUser.getFirstname()).ifPresent(existingUser::setFirstname);
                    Optional.ofNullable(updatedUser.getLastname()).ifPresent(existingUser::setLastname);
                    Optional.ofNullable(updatedUser.getEmail()).ifPresent(existingUser::setEmail);
                    Optional.ofNullable(updatedUser.getCredits()).ifPresent(existingUser::setCredits);

                    User updated = userRepository.save(existingUser);

                    return Optional.of(updated);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public void forceDeleteUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.ifPresent(user -> {

            ticketRepository.deleteByUserId(id);

            List<Event> userEvents = user.getPublishedEvents();
            for (Event event : userEvents) {
                notificationService.sendEventNotification("Event canceled.", event.getName()
                        + " is canceled, your money has been refunded.", event.getId());
                ticketService.refundUsersForCanceledEvent(event.getId());
                eventRepository.deleteById(event.getId());
            }

            List<Comment> userComments = commentRepository.findAllByCommenterId(id);
            for (Comment comment : userComments) {
                commentRepository.deleteById(comment.getId());
            }

            List<Notification> userNotifications = user.getNotifications();
            for (Notification notification : userNotifications) {
                notificationRepository.deleteById(notification.getId());
            }

            authenticationService.deleteAllUserTokens(id);
            userRepository.deleteById(id);

        });
    }
}
