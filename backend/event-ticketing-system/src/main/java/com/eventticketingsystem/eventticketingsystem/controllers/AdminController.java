package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.RedeemCode;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.AdminService;
import com.eventticketingsystem.eventticketingsystem.services.RedeemCodeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    private final RedeemCodeService redeemCodeService;
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
    /**
     * Forcefully delete a user by ID.
     *
     * @param id The UUID of the user to be forcefully deleted.
     * @return ResponseEntity with a success message or throw UserNotFoundException if user not found.
     */
    @DeleteMapping("user/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @Transactional
    public ResponseEntity<String> forceDeleteUserById(@PathVariable UUID id) {
        try {
            adminService.forceDeleteUserById(id);
            return ResponseEntity.ok("User forcefully deleted");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }
    /**
     * Edits a redeem code in the database.
     *
     * @param id The ID of the redeem code to edit.
     * @param updatedRedeemCode The updated redeem code.
     * @return ResponseEntity containing the edited redeem code.
     */
    @PutMapping("redeem/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<RedeemCode> editRedeemCode(@PathVariable UUID id, @RequestBody @Valid RedeemCode updatedRedeemCode) {
        Optional<RedeemCode> editedRedeemCode = redeemCodeService.editRedeemCode(id, updatedRedeemCode);
        return editedRedeemCode
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Retrieves all redeem codes from the database.
     *
     * @return ResponseEntity containing the list of redeem codes.
     */
    @GetMapping("redeem-codes")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<RedeemCode>> getAllRedeemCodes() {
        return ResponseEntity.ok(redeemCodeService.getAllRedeemCodes());
    }
    /**
     * Deletes a redeem code from the database by its ID.
     *
     * @param id The ID of the redeem code to delete.
     * @return A ResponseEntity with a success message if the redeem code is successfully deleted,
     *         or an error message if deletion fails.
     */
    @DeleteMapping("redeem/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @Transactional
    public ResponseEntity<String> deleteRedeemCodeById(@PathVariable UUID id) {
        try {
            redeemCodeService.deleteRedeemCodeById(id);
            return ResponseEntity.ok("Redeem code forcefully deleted");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete redeem code: " + e.getMessage());
        }
    }
    /**
     * Adds a new redeem code to the database.
     *
     * @param redeemCode The redeem code to add.
     * @return A ResponseEntity with the added redeem code if successful, or an error message if addition fails.
     */
    @PostMapping("redeem")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<String> addRedeemCode(@RequestBody @Valid RedeemCode redeemCode) {
        try {
            redeemCodeService.addRedeemCode(redeemCode);
            return ResponseEntity.ok("Redeem Code added successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete redeem code: " + e.getMessage());
        }
    }
}
