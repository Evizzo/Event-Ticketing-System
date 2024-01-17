package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.Review;
import com.eventticketingsystem.eventticketingsystem.services.ReviewService;
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
@RequestMapping("review")
public class ReviewController {
    private final ReviewService reviewService;
    public static final String REVIEW_NOT_FOUND = "Review not found with ID: ";

    @PostMapping("/{eventId}")
    public ResponseEntity<Review> saveReview(@Valid @RequestBody Review review, @PathVariable UUID eventId, HttpServletRequest request){
        return ResponseEntity.ok(reviewService.saveReview(review, eventId, request));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteReview(@PathVariable UUID id, HttpServletRequest request) {
        return reviewService.findReviewById(id)
                .map(review -> {
                    reviewService.deleteReviewById(id, request);
                    return ResponseEntity.ok("Review deleted successfully.");
                })
                .orElseThrow(() -> new RuntimeException(REVIEW_NOT_FOUND + id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Review>> retrieveAllReviewsForEvent(@PathVariable UUID id){
        return ResponseEntity.ok(reviewService.getAllReviewsForEvent(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable UUID id, @Valid @RequestBody Review review, HttpServletRequest request) {
        return reviewService.updateReview(id, review, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException(REVIEW_NOT_FOUND + id));
    }
}
