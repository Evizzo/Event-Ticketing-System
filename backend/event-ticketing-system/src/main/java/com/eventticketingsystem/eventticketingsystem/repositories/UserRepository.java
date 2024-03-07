package com.eventticketingsystem.eventticketingsystem.repositories;

import com.eventticketingsystem.eventticketingsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT u.credits FROM User u WHERE u.id = :userId")
    Optional<BigDecimal> findCreditsByUserId(@Param("userId") UUID userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.publishedEvents WHERE u.email = :email")
    User findUserWithPublishedEvents(String email);
}