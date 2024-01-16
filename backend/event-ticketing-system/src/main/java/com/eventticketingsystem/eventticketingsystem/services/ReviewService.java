package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.config.JwtService;
import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.Review;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.ReviewRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("Unaothorized access");
        }
    }
}
