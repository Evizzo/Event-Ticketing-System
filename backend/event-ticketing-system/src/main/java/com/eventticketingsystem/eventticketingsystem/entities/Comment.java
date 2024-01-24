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
import java.util.HashSet;
import java.util.Set;
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
    private Integer likes;
    private Integer dislikes;
    @ElementCollection
    @CollectionTable(name = "comment_likes", joinColumns = @JoinColumn(name = "comment_id"))
    @Column(name = "user_id")
    private Set<UUID> likedByUsers = new HashSet<>();
    @ElementCollection
    @CollectionTable(name = "comment_dislikes", joinColumns = @JoinColumn(name = "comment_id"))
    @Column(name = "user_id")
    private Set<UUID> dislikedByUsers = new HashSet<>();
}
