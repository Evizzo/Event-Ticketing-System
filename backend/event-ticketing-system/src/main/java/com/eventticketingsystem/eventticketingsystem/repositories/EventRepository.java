package com.eventticketingsystem.eventticketingsystem.repositories;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByPublisherId(UUID publisherId);

    List<Event> findAllByOrderByDateAsc();

    List<Event> findAllByOrderByNameAsc();

    List<Event> findAllByOrderByTicketPriceAsc();

    List<Event> findByPublisherIdOrderByDateAsc(UUID publisherId);

    List<Event> findByPublisherIdOrderByNameAsc(UUID publisherId);

    List<Event> findByPublisherIdOrderByTicketPriceAsc(UUID publisherId);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.id IN ( " +
            "   SELECT t.event.id " +
            "   FROM Ticket t " +
            "   GROUP BY t.event " +
            "   ORDER BY COUNT(t) DESC " +
            ") " +
            "ORDER BY e.name ASC")
    List<Event> findMostPopularEventsSortedByName(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.commentCount = e.commentCount + 1 WHERE e.id = :eventId")
    void incrementCommentCount(UUID eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.commentCount = e.commentCount - 1 WHERE e.id = :eventId")
    void decrementCommentCount(UUID eventId);
}