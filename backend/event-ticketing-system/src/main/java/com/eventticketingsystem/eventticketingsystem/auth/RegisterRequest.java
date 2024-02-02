package com.eventticketingsystem.eventticketingsystem.auth;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "Invalid email format")
    private String email;
    @Size(min = 7, message = "Password must be at least 7 characters long")
    @Pattern(regexp = "^(?=.*[0-9]).{7,}$", message = "Password must have at least 7 characters and contain at least one number")
    private String password;
}
