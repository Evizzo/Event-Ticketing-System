package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Notification;
import com.eventticketingsystem.eventticketingsystem.exceptions.EventNotFoundException;
import com.eventticketingsystem.eventticketingsystem.exceptions.UserNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtService jwtService;
    @GetMapping
    public ResponseEntity<List<Notification>> retrieveAllUserNotifications(HttpServletRequest request){
        return ResponseEntity.ok(notificationService.retrieveAllUserNotifications(jwtService.extractUserIdFromToken(request)));
    }
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteNotification(@PathVariable UUID id) {
        return notificationService.findNotificationById(id)
                .map(notification -> {
                    notificationService.deleteNotificationById(id);
                    return ResponseEntity.ok("Notification deleted successfully.");
                })
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }
}
