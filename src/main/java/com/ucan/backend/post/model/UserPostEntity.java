package com.ucan.backend.post.model;

import com.ucan.backend.tag.model.TagEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_post_votes", joinColumns = @JoinColumn(name = "post_id"))
  @MapKeyColumn(name = "user_id")
  @Column(name = "vote", columnDefinition = "boolean") // true for upvote and false for downvote
  private Map<Long, Boolean> userVotes = new HashMap<>();

  @Column(length = 1000)
  private String description;

  @Column(name = "creator_id", nullable = false)
  private Long creatorId;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "is_moderated", nullable = false)
  private boolean isModerated;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "post_tags",
      joinColumns = @JoinColumn(name = "post_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<TagEntity> tags = new HashSet<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
