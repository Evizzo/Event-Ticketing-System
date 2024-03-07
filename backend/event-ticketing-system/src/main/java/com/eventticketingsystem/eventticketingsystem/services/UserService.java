package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.auth.AuthenticationService;
import com.eventticketingsystem.eventticketingsystem.auth.ChangePasswordRequest;
import com.eventticketingsystem.eventticketingsystem.entities.*;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final AuthenticationService authenticationService;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    public User saveUser(User user){
        return userRepository.save(user);
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> findUserById(UUID userId){
        return userRepository.findById(userId);
    }
    public void deleteUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.ifPresent(user -> {
            List<Event> userEvents = user.getPublishedEvents();

            for (Event event : userEvents) {
                if (!event.isDone()) throw new RuntimeException("You have events going on !");
            }

            ticketRepository.deleteByUserId(id);

            for (Event event : userEvents) {
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

    public BigDecimal retrieveUserCredits(UUID id){
        return userRepository.findCreditsByUserId(id)
                .orElseThrow(() -> new RuntimeException("Credits not found for user with ID: " + id));
    }
    public Optional<User> updateUser(UUID id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    Optional.ofNullable(updatedUser.getFirstname()).ifPresent(existingUser::setFirstname);
                    Optional.ofNullable(updatedUser.getLastname()).ifPresent(existingUser::setLastname);
                    Optional.ofNullable(updatedUser.getPassword()).ifPresent(existingUser::setPassword);
                    Optional.ofNullable(updatedUser.getEmail()).ifPresent(existingUser::setEmail);
                    Optional.ofNullable(updatedUser.getCredits()).ifPresent(existingUser::setCredits);

                    User updated = userRepository.save(existingUser);

                    return Optional.of(updated);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public Optional<List<Ticket>> retrieveAllUserTickets(String sortCriteria, UUID userId) {
        return switch (sortCriteria) {
            case "date" -> Optional.of(ticketRepository.findAllByUserIdOrderByEvent_Date(userId));
            case "price" -> Optional.of(ticketRepository.findAllByUserIdOrderByPaidAmount(userId));
            default -> Optional.of(ticketRepository.findAllByUserIdOrderByEvent_Name(userId));
        };
    }
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
    public User getUserWithPublishedEvents(String email) {
        return userRepository.findUserWithPublishedEvents(email);
    }
}
