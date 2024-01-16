package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.entities.Review;
import com.eventticketingsystem.eventticketingsystem.services.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("review")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/{eventId}")
    public ResponseEntity<Review> saveReview(@Valid @RequestBody Review review, @PathVariable UUID eventId, HttpServletRequest request){
        return ResponseEntity.ok(reviewService.saveReview(review,eventId,request));
    }
}
