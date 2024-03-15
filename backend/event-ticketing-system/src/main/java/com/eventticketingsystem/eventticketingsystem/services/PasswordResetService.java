package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.auth.RestartPasswordRequest;
import com.eventticketingsystem.eventticketingsystem.entities.PasswordResetToken;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.repositories.PasswordResetTokenRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PasswordResetService {
    private UserRepository userRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void sendPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        System.out.println(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            PasswordResetToken token = new PasswordResetToken();
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(LocalDate.now().plusDays(1));
            token.setUser(user);
            passwordResetTokenRepository.save(token);

            String resetLink = "http://localhost:5173/reset-password/" + token.getToken();
            emailService.sendEmail(user.getEmail(), "Password Reset", "Click the following link to reset your password: " + resetLink +
                    ". Link expires at: " + LocalDate.now().plusDays(1));
        } else {
            throw new UserNotFoundException("User with the given email is not found.");
        }
    }
    public void resetPasswordWithToken(String tokenValue, String newPassword) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token."));

        if (token.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Token has expired.");
        }

        User user = token.getUser();

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("New password must be different from the old one.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(token);
    }
}
