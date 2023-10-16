package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.database.Event;
import com.eventticketingsystem.eventticketingsystem.database.Ticket;
import com.eventticketingsystem.eventticketingsystem.database.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.TicketRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
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
        ticketRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    public Optional<User> updateUser(UUID id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (updatedUser.getFirstname() != null) {
                        existingUser.setFirstname(updatedUser.getFirstname());
                    }
                    if (updatedUser.getLastname() != null) {
                        existingUser.setLastname(updatedUser.getLastname());
                    }
                    if (updatedUser.getPassword() != null) {
                        existingUser.setPassword(updatedUser.getPassword());
                    }
                    if (updatedUser.getEmail() != null) {
                        existingUser.setEmail(updatedUser.getEmail());
                    }
                    if (updatedUser.getCredits() != null) {
                        existingUser.setCredits(updatedUser.getCredits());
                    }

                    User updated = userRepository.save(existingUser);
                    return Optional.of(updated);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
    public Optional<List<Ticket>> retrieveAllUserTickets(UUID userId) {
        return Optional.of(ticketRepository.findTicketsPurchasedByUserId(userId));
    }
}
