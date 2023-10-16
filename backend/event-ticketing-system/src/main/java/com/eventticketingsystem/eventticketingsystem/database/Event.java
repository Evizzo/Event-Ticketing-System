package com.eventticketingsystem.eventticketingsystem.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Size(max = 32, min = 2, message = "Max character size is 32, min is 2")
    private String name;
    // Added validation that all events should be in the future
    @Future(message = "Event Date should be in the future")
    private LocalDate date;
    @Size(max = 32, min = 3, message = "Max character size is 32, min is 3")
    private String location;
    @Size(max = 320, min = 12, message = "Max character size is 320, min is 12")
    private String description;
    private int capacity;
    private BigDecimal ticketPrice;
}
