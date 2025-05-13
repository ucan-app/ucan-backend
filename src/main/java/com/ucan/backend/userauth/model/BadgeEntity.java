package com.ucan.backend.userauth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "badge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeEntity {
  @EmbeddedId private BadgeId id;

  @Column(nullable = false)
  private boolean validated;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false)
  private UserAuthEntity user;

  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = new BadgeId();
    }
    id.setUserId(user.getId());
  }
}
