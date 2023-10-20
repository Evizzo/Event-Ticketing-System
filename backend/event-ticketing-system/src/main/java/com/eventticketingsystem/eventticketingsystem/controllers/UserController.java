package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.entities.User;
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
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    public static final String USER_NOT_FOUND = "User not found with ID: ";
    @PostMapping
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    @GetMapping
    public ResponseEntity<List<User>> retriveAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> retrieveUser(@PathVariable UUID id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + id));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        return userService.findUserById(id)
                .map(user -> {
                    userService.deleteUserById(id);
                    return ResponseEntity.ok("User deleted successfully.");
                })
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + id));
    }
    @GetMapping("/tickets/{userId}")
    public ResponseEntity<List<Ticket>> retrieveAllUserTickets(@PathVariable UUID userId) {
        return userService.retrieveAllUserTickets(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
