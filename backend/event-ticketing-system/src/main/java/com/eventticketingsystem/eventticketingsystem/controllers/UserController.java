package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.auth.ChangePasswordRequest;
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
import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing user-related operations.
 */
@AllArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    public static final String USER_NOT_FOUND = "User not found with ID: ";
    /**
     * Retrieves the credits of the currently authenticated user.
     *
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @return ResponseEntity containing the user's credits.
     */
    @GetMapping("/credits")
    public ResponseEntity<BigDecimal> retrieveUserCredits(HttpServletRequest request){
        return ResponseEntity.ok(userService.retrieveUserCredits(jwtService.extractUserIdFromToken(request)));
    }
    /**
     * Adds a new user to the system, intended for Admins.
     *
     * @param user The user object to be added.
     * @return ResponseEntity containing the newly added user.
     */
    @PostMapping
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> retrieveAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @return ResponseEntity containing the details of the current user.
     * @throws UserNotFoundException if the user is not found.
     */
    @GetMapping("/current")
    public ResponseEntity<User> retrieveCurrentUser(HttpServletRequest request) {
        return userService.findUserById(jwtService.extractUserIdFromToken(request))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + jwtService.extractUserIdFromToken(request)));
    }
    /**
     * Deletes the currently authenticated user.
     *
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @return ResponseEntity indicating the success of the deletion.
     * @throws UserNotFoundException if the user is not found.
     */
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
    /**
     * Updates the details of the currently authenticated user.
     *
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @param user The updated user object.
     * @return ResponseEntity containing the updated user details.
     * @throws UserNotFoundException if the user is not found.
     */
    @PutMapping("/update-current-user")
    public ResponseEntity<User> updateUser(HttpServletRequest request, @Valid @RequestBody User user) {
        return userService.updateUser(jwtService.extractUserIdFromToken(request), user)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + jwtService.extractUserIdFromToken(request)));
    }
    /**
     * Retrieves all tickets associated with the currently authenticated user.
     *
     * @param sortCriteria The criteria for sorting the tickets.
     * @param request HttpServletRequest to extract the user ID from the JWT token.
     * @return ResponseEntity containing a list of user tickets.
     */
    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> retrieveAllUserTickets(@RequestParam String sortCriteria, HttpServletRequest request) {
        return userService.retrieveAllUserTickets(sortCriteria, jwtService.extractUserIdFromToken(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Endpoint for changing user password.
     *
     * @param request        The request object containing current and new password.
     * @param connectedUser  The principal object representing the authenticated user.
     * @return               ResponseEntity indicating the success of the password change operation.
     */
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    /**
     * Retrieves user information (user profile) along with the list of events they published.
     *
     * @param email The unique identifier of the user.
     * @return The user information with the list of published events.
     */
    @GetMapping("/profile/{email}")
    public User getUserWithPublishedEvents(@PathVariable String email) {
        return userService.getUserWithPublishedEvents(email);
    }
}