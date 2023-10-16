package com.eventticketingsystem.eventticketingsystem.repositories;

import com.eventticketingsystem.eventticketingsystem.database.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Modifying
    @Query("DELETE FROM Ticket t WHERE t.user.id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);

    void deleteByEventId(UUID id);

    @Modifying
    @Query("SELECT t FROM Ticket t WHERE t.user.id = :userId")
    List<Ticket> findTicketsPurchasedByUserId(@Param("userId") UUID userId);
}