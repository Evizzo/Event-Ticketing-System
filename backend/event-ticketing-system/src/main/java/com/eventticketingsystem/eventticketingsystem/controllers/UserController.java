package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Ticket;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    public static final String USER_NOT_FOUND = "User not found with ID: ";
    @GetMapping("/credits")
    public ResponseEntity<BigDecimal> retrieveUserCredits(HttpServletRequest request){
        return ResponseEntity.ok(userService.retrieveUserCredits(jwtService.extractUserIdFromToken(request)));
    }
    @PostMapping
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    @GetMapping
    public ResponseEntity<List<User>> retriveAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("/current")
    public ResponseEntity<User> retrieveCurrentUser(HttpServletRequest request) {
        return userService.findUserById(jwtService.extractUserIdFromToken(request))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + jwtService.extractUserIdFromToken(request)));
    }
    @DeleteMapping("/delete-current-user")
    @Transactional
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        return userService.findUserById(jwtService.extractUserIdFromToken(request))
                .map(user -> {
                    userService.deleteUserById(jwtService.extractUserIdFromToken(request));
                    return ResponseEntity.ok("User deleted successfully.");
                })
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + jwtService.extractUserIdFromToken(request)));
    }
    @PutMapping("/update-current-user")
    public ResponseEntity<User> updateUser(HttpServletRequest request, @Valid @RequestBody User user) {
        return userService.updateUser(jwtService.extractUserIdFromToken(request), user)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + jwtService.extractUserIdFromToken(request)));
    }
    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> retrieveAllUserTickets(@RequestParam String sortCriteria, HttpServletRequest request) {
        return userService.retrieveAllUserTickets(sortCriteria, jwtService.extractUserIdFromToken(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
