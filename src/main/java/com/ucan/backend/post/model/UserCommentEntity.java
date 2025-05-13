package com.ucan.backend.post.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "user_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCommentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long postId;

  @Column(nullable = false)
  private Long authorId;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  private int replyCount;

  private Instant createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
  }
}
