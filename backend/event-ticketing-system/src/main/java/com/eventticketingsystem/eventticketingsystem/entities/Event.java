package com.eventticketingsystem.eventticketingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    @Size(max = 32, min = 2, message = "Max character size of name is 32, min is 2")
    private String name;
    @Future(message = "Event Date should be in the future")
    private LocalDate date;
    @Size(max = 32, min = 2, message = "Max character size of location is 32, min is 3")
    private String location;
    @Size(max = 320, min = 12, message = "Max character size of description is 320, min is 12")
    private String description;
    @Min(value = 0, message = "Capacity must be positive")
    private Integer capacity;
    @DecimalMin(value = "0", message = "Ticket price cannot be negative")
    private BigDecimal ticketPrice;
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @JsonIgnore
    private User publisher;
    private boolean isDone;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Comment> comments;
    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

}
