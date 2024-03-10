package com.eventticketingsystem.eventticketingsystem.repositories;

import com.eventticketingsystem.eventticketingsystem.entities.RedeemCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RedeemCodeRepository extends JpaRepository<RedeemCode, UUID> {
    Optional<RedeemCode> findByName(String name);
}
