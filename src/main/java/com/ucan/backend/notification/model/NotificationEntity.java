package com.ucan.backend.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class NotificationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long recipientId;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private boolean read = false;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;
}
