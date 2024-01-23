package com.eventticketingsystem.eventticketingsystem.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;
    @ManyToOne
    @JoinColumn(name = "commenter_id")
    @JsonIgnore
    private User commenter;
    @Size(max = 320, min = 3, message = "Max character size of comment is 320, min is 3")
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;
    private boolean isEdited;
    private String emailOfCommenter;
}
