package com.ucan.backend.userprofile.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long userId; // FK to userauth

  @Column(nullable = false)
  private String fullName;

  private String linkedinUrl;

  private String personalWebsite;

  private String bio;

  private Integer graduationYear;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_badges", joinColumns = @JoinColumn(name = "profile_id"))
  @Column(name = "badge")
  private List<String> badges;

  private Instant createdAt;

  private Instant updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
  }
}
