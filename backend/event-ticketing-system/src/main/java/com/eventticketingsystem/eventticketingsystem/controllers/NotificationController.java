package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Notification;
import com.eventticketingsystem.eventticketingsystem.exceptions.NotificationNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * Controller class for managing notification-related operations.
 */
@AllArgsConstructor
@RestController
@RequestMapping("notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtService jwtService;
    /**
     * Retrieves all notifications for the current logged-in user.
     *
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the list of user notifications.
     */
    @GetMapping
    public ResponseEntity<List<Notification>> retrieveAllUserNotifications(HttpServletRequest request){
        return ResponseEntity.ok(notificationService.retrieveAllUserNotifications(jwtService.extractUserIdFromToken(request)));
    }
    /**
     * Deletes a specific notification by its ID.
     *
     * @param id UUID of the notification to be deleted.
     * @return ResponseEntity indicating the success of the deletion.
     * @throws NotificationNotFoundException if the notification with the given ID is not found.
     */
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteNotification(@PathVariable UUID id) {
        return notificationService.findNotificationById(id)
                .map(notification -> {
                    notificationService.deleteNotificationById(id);
                    return ResponseEntity.ok("Notification deleted successfully.");
                })
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
    }
}
