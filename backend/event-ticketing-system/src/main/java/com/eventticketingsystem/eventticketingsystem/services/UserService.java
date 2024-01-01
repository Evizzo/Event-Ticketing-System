package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.auth.AuthenticationService;
import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final AuthenticationService authenticationService;
    public User saveUser(User user){
        return userRepository.save(user);
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> findUserById(UUID userId){
        return userRepository.findById(userId);
    }
    public void deleteUserById(UUID id){
        authenticationService.deleteAllUserTokens(id);
        ticketRepository.deleteByUserId(id);
        userRepository.deleteById(id);
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
}
