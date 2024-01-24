package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.Comment;
import com.eventticketingsystem.eventticketingsystem.services.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    public static final String COMMENT_NOT_FOUND = "Comment not found with ID: ";

    @PostMapping("/{eventId}")
    public ResponseEntity<Comment> saveComment(@Valid @RequestBody Comment comment, @PathVariable UUID eventId, HttpServletRequest request){
        return ResponseEntity.ok(commentService.saveComment(comment, eventId, request));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteComment(@PathVariable UUID id, HttpServletRequest request) {
        return commentService.findCommentById(id)
                .map(comment -> {
                    commentService.deleteCommentById(id, request);
                    return ResponseEntity.ok("Comment deleted successfully.");
                })
                .orElseThrow(() -> new RuntimeException(COMMENT_NOT_FOUND + id));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<Comment>> getAllCommentsForEventByDate(@PathVariable UUID eventId){
        return ResponseEntity.ok(commentService.getAllCommentsForEventByDate(eventId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable UUID id, @Valid @RequestBody Comment comment, HttpServletRequest request) {
        return commentService.updateComment(id, comment, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException(COMMENT_NOT_FOUND + id));
    }
    @PutMapping("/{id}/like")
    public ResponseEntity<Comment> likeComment(@PathVariable UUID id, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.likeComment(id, request));
    }

    @PutMapping("/{id}/dislike")
    public ResponseEntity<Comment> dislikeComment(@PathVariable UUID id, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.dislikeComment(id, request));
    }
}
