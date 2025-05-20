package com.ucan.backend.post.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "user_posts")
@Getter
@Setter
public class UserPostEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private int upvote = 0;

  @Column(nullable = false)
  private int downvote = 0;

  @Column(length = 1000)
  private String description;

  @Column(name = "creator_id", nullable = false)
  private Long creatorId;

  @Column(name = "is_moderated", nullable = false)
  private boolean isModerated;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
