package com.eventticketingsystem.eventticketingsystem.repositories;

import com.eventticketingsystem.eventticketingsystem.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query("""
                select t from Token t inner join User u on t.user.id = u.id
                where u.id = :userId and (t.expired = false or t.revoked = false)
           """)
    List<Token> findAllValidTokensByUser(UUID userId);

    Optional<Token> findByToken(String token);
}
