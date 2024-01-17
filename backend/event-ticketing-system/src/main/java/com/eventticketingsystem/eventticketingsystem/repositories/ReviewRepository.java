package com.eventticketingsystem.eventticketingsystem.repositories;

import com.eventticketingsystem.eventticketingsystem.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByEvent_Id(UUID eventId);
}
