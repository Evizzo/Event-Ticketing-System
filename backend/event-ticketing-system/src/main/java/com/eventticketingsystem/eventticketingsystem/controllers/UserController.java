package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.database.Ticket;
import com.eventticketingsystem.eventticketingsystem.database.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    @PostMapping("user")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    @GetMapping("user")
    public ResponseEntity<List<User>> retriveAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("user/{id}")
    public ResponseEntity<User> retrieveUser(@PathVariable UUID id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new UserNotFoundException("id: " + id));
    }
    @DeleteMapping("user/{id}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        return userService.findUserById(id)
                .map(user -> {
                    userService.deleteUserById(id);
                    return ResponseEntity.ok("User deleted successfully.");
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(updatedUser -> ResponseEntity.ok(updatedUser))
                .orElseThrow(() -> new UserNotFoundException("id: " + id));
    }
    @GetMapping("/user/tickets/{userId}")
    public ResponseEntity<List<Ticket>> retrieveAllUserTickets(@PathVariable UUID userId) {
        return userService.retrieveAllUserTickets(userId)
                .map(userTickets -> ResponseEntity.ok(userTickets))
                .orElse(ResponseEntity.notFound().build());
    }
}
