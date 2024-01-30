package com.eventticketingsystem.eventticketingsystem.auth;

import com.eventticketingsystem.eventticketingsystem.config.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
/**
 * Controller class for handling authentication-related operations.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;
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
}
