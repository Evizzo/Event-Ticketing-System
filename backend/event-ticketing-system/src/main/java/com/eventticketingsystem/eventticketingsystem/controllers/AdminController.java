package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.eventticketingsystem.eventticketingsystem.controllers.UserController.USER_NOT_FOUND;
/**
 * Controller class to handle administrative operations.
 */
@AllArgsConstructor
@RestController
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;
    /**
     * Add a new user.
     *
     * @param user The user object to be added.
     * @return ResponseEntity with the added user.
     */
    @PostMapping("user")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(adminService.saveUser(user));
    }
    /**
     * Retrieve all users.
     *
     * @return ResponseEntity with a list of all users.
     */
    @GetMapping("users")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<User>> retrieveAllUsers(){
        return ResponseEntity.ok(adminService.findAllUsers());
    }
    /**
     * Update an existing user.
     *
     * @param id   The UUID of the user to be updated.
     * @param user The updated user object.
     * @return ResponseEntity with the updated user.
     * @throws UserNotFoundException if the user with the given id is not found.
     */
    @PutMapping("user")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<User> updateUser(@RequestParam UUID id, @Valid @RequestBody User user) {
        return adminService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + id));
    }

}
