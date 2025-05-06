package com.ucan.backend.userauth.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class UserAuthEntityTest {

  @Test
  void builder_ShouldCreateEntityWithDefaultValues() {
    // Act
    UserAuthEntity entity =
        UserAuthEntity.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .build();

    // Assert
    assertThat(entity.getId()).isNull();
    assertThat(entity.getUsername()).isEqualTo("testuser");
    assertThat(entity.getEmail()).isEqualTo("test@example.com");
    assertThat(entity.getPassword()).isEqualTo("password");
    assertThat(entity.isEnabled()).isTrue(); // Default value
    assertThat(entity.getCreatedAt()).isNull();
  }

  @Test
  void builder_ShouldCreateEntityWithAllValues() {
    // Arrange
    Instant now = Instant.now();

    // Act
    UserAuthEntity entity =
        UserAuthEntity.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .enabled(false)
            .createdAt(now)
            .build();

    // Assert
    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getUsername()).isEqualTo("testuser");
    assertThat(entity.getEmail()).isEqualTo("test@example.com");
    assertThat(entity.getPassword()).isEqualTo("password");
    assertThat(entity.isEnabled()).isFalse();
    assertThat(entity.getCreatedAt()).isEqualTo(now);
  }

  @Test
  void onCreate_ShouldSetCreatedAt() {
    // Arrange
    UserAuthEntity entity =
        UserAuthEntity.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .build();

    // Act
    entity.onCreate();

    // Assert
    assertThat(entity.getCreatedAt()).isNotNull();
    assertThat(entity.getCreatedAt()).isBeforeOrEqualTo(Instant.now());
  }
}
