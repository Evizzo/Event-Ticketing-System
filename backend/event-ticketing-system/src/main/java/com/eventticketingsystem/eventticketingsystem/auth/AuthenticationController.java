package com.eventticketingsystem.eventticketingsystem.auth;

import com.eventticketingsystem.eventticketingsystem.config.LogoutService;
import com.eventticketingsystem.eventticketingsystem.services.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller class for handling authentication-related operations.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;
    private final PasswordResetService passwordResetService;
    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details (validated using @Valid annotation).
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    /**
     * Authenticates a user with the provided credentials.
     *
     * @param request The authentication request containing user credentials.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
            ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    /**
     * Logs out the currently authenticated user.
     *
     * @param request  HttpServletRequest representing the current request.
     * @param response HttpServletResponse representing the current response.
     * @return ResponseEntity indicating the success of the logout operation.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok().build();
    }
    /**
     * Endpoint to send a password reset email to the provided email address.
     *
     * @param email The email address of the user requesting a password reset.
     * @return ResponseEntity indicating the status of the password reset email sending operation.
     */
    @PostMapping("/send-email")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestParam String email) {
        try {
            passwordResetService.sendPasswordReset(email);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while sending password reset email: " + e.getMessage());
        }
    }
    /**
     * Endpoint to reset the password using the provided token and new password.
     *
     * @param request Request object containing the token and new password for resetting.
     * @return ResponseEntity indicating the status of the password reset operation.
     * @throws RuntimeException If an error occurs while resetting the password.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPasswordWithToken(
            @Valid @RequestBody RestartPasswordRequest request) {
        try {
            passwordResetService.resetPasswordWithToken(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error resetting password: " + e.getMessage());
        }
    }
}
