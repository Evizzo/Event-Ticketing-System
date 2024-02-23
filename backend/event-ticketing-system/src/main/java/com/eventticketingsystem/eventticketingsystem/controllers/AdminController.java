package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.services.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;
    @PostMapping("user")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(adminService.saveUser(user));
    }
    @GetMapping("users")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<User>> retrieveAllUsers(){
        return ResponseEntity.ok(adminService.findAllUsers());
    }
}
