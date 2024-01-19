package com.eventticketingsystem.eventticketingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    @JsonIgnore
    private User reviewer;
    @Size(max = 320, message = "Max size of comment is 320 characters")
    private String comment;
    private LocalDate date;
    private boolean isEdited;
    private String emailOfReviewer;
}
