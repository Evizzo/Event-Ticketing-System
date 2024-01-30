package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.Comment;
import com.eventticketingsystem.eventticketingsystem.exceptions.CommentNotFoundException;
import com.eventticketingsystem.eventticketingsystem.services.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * Controller class for managing comment-related operations.
 */
@AllArgsConstructor
@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    public static final String COMMENT_NOT_FOUND = "Comment not found with ID: ";
    /**
     * Saves a new comment for a specific event.
     *
     * @param comment The comment to be saved (validated using @Valid annotation).
     * @param eventId UUID of the event to which the comment belongs.
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the saved Comment.
     */
    @PostMapping("/{eventId}")
    public ResponseEntity<Comment> saveComment(@Valid @RequestBody Comment comment, @PathVariable UUID eventId, HttpServletRequest request){
        return ResponseEntity.ok(commentService.saveComment(comment, eventId, request));
    }
    /**
     * Deletes a specific comment by its ID.
     *
     * @param id      UUID of the comment to be deleted.
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity indicating the success of the deletion.
     * @throws CommentNotFoundException if the comment with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteComment(@PathVariable UUID id, HttpServletRequest request) {
        return commentService.findCommentById(id)
                .map(comment -> {
                    commentService.deleteCommentById(id, request);
                    return ResponseEntity.ok("Comment deleted successfully.");
                })
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND + id));
    }
    /**
     * Retrieves all comments for a specific event.
     *
     * @param sortCriteria Sorting criteria for the retrieved comments.
     * @param eventId UUID of the event for which comments are to be retrieved.
     * @return ResponseEntity containing the list of comments for the specified event.
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<List<Comment>> getAllCommentsForEvent(
            @PathVariable UUID eventId,
            @RequestParam(defaultValue = "date") String sortCriteria
    ) {
        return ResponseEntity.ok(commentService.getAllCommentsForEvent(eventId, sortCriteria));
    }
    /**
     * Updates a specific comment by its ID.
     *
     * @param id      UUID of the comment to be updated.
     * @param comment The updated comment (validated using @Valid annotation).
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the updated Comment.
     * @throws CommentNotFoundException if the comment with the given ID is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable UUID id, @Valid @RequestBody Comment comment, HttpServletRequest request) {
        return commentService.updateComment(id, comment, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND + id));
    }
    /**
     * Likes a specific comment by its ID.
     *
     * @param id      UUID of the comment to be liked.
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the liked Comment.
     */
    @PutMapping("/{id}/like")
    public ResponseEntity<Comment> likeComment(@PathVariable UUID id, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.likeComment(id, request));
    }
    /**
     * Dislikes a specific comment by its ID.
     *
     * @param id      UUID of the comment to be disliked.
     * @param request HttpServletRequest containing the JWT token for extracting the current logged-in user.
     * @return ResponseEntity containing the disliked Comment.
     */
    @PutMapping("/{id}/dislike")
    public ResponseEntity<Comment> dislikeComment(@PathVariable UUID id, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.dislikeComment(id, request));
    }
}
