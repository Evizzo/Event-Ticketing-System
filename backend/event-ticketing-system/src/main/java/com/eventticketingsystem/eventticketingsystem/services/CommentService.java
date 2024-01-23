package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.Comment;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.CommentRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final NotificationService notificationService;
    public Comment saveComment(Comment comment, UUID eventId, HttpServletRequest request) {
        UUID userId = jwtService.extractUserIdFromToken(request);
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User commenter = optionalUser.get();
            Event event = optionalEvent.get();

            if (event.isDone()) {
                comment.setCommenter(commenter);
                comment.setEvent(event);
                comment.setDate(LocalDateTime.now());
                comment.setEdited(false);
                comment.setEmailOfCommenter(commenter.getEmail());

                notificationService.sendCommentNotification("New comment", commenter.getEmail()
                        + " left a comment on "
                        + event.getName(), event.getPublisher().getId());

                return commentRepository.save(comment);
            } else {
                throw new RuntimeException("Unauthorized access.");
            }
        } else {
            throw new RuntimeException("User or event not found");
        }
    }

    public Optional<Comment> findCommentById(UUID id){
        return commentRepository.findById(id);
    }

    public void deleteCommentById(UUID id, HttpServletRequest request) {
        UUID commenterIdFromToken = jwtService.extractUserIdFromToken(request);
        Optional<Comment> optionalComment = commentRepository.findById(id);

        optionalComment.ifPresent(comment -> {
            UUID commenterId = comment.getCommenter().getId();
            if (commenterId.equals(commenterIdFromToken)) {
                notificationService.sendCommentNotification("Deleted comment", comment.getEmailOfCommenter()
                        + " deleted a comment on "
                        + comment.getEvent().getName(), comment.getEvent().getPublisher().getId());
                commentRepository.deleteById(id);
            } else {
                throw new RuntimeException("You are not authorized to delete this comment.");
            }
        });
    }

    public List<Comment> getAllCommentsForEventByDate(UUID eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            return event.getComments()
                    .stream()
                    .sorted(Comparator.comparing(Comment::getDate).reversed())
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }
    }

    public Optional<Comment> updateComment(UUID id, Comment updatedComment, HttpServletRequest request) {
        UUID userIdFromToken = jwtService.extractUserIdFromToken(request);

        return commentRepository.findById(id)
                .map(existingComment -> {
                    User commenter = existingComment.getCommenter();
                    if (commenter == null) {
                        throw new RuntimeException("Commenter information is missing for this event.");
                    }

                    UUID commenterId = commenter.getId();
                    if (userIdFromToken.equals(commenterId)) {
                        Optional.ofNullable(updatedComment.getComment()).ifPresent(existingComment::setComment);
                        existingComment.setEdited(true);
                        existingComment.setDate(LocalDateTime.now());

                        notificationService.sendCommentNotification("Updated comment", commenter.getEmail()
                                + " updated a comment on "
                                + existingComment.getEvent().getName(), existingComment.getEvent().getPublisher().getId());

                        Comment updated = commentRepository.save(existingComment);

                        return Optional.of(updated);
                    } else {
                        throw new RuntimeException("You are not authorized to update this comment.");
                    }
                })
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));
    }
}
