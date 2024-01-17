package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.Review;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.ReviewRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import io.micrometer.observation.ObservationFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    public Review saveReview(Review review, UUID eventId, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findById(jwtService.extractUserIdFromToken(request));
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User reviewer = optionalUser.get();
            Event event = optionalEvent.get();
            review.setReviewer(reviewer);
            review.setEvent(event);
            return reviewRepository.save(review);
        } else {
            throw new RuntimeException("Unauthorized access");
        }
    }

    public Optional<Review> findReviewById(UUID id){
        return reviewRepository.findById(id);
    }

    public void deleteReviewById(UUID id, HttpServletRequest request) {
        UUID reviewerIdFromToken = jwtService.extractUserIdFromToken(request);
        Optional<Review> optionalReview = reviewRepository.findById(id);

        optionalReview.ifPresent(review -> {
            UUID reviewerId = review.getReviewer().getId();
            if (reviewerId.equals(reviewerIdFromToken)) {
                reviewRepository.deleteById(id);
            } else {
                throw new RuntimeException("You are not authorized to delete this review.");
            }
        });
    }

    public List<Review> getAllReviewsForEvent(UUID eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            return event.getReviews();
        } else {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }
    }
}
