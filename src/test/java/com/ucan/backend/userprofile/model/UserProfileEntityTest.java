package com.ucan.backend.userprofile.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserProfileEntityTest {

  @Test
  void builder_ShouldCreateEntityWithDefaults() {
    UserProfileEntity entity =
        UserProfileEntity.builder()
            .userId(200L)
            .fullName("Ali Farhadi")
            .bio("CEO of AI at Allen School")
            .graduationYear(2006)
            .badges(List.of("Apple"))
            .build();

    assertThat(entity.getUserId()).isEqualTo(200L);
    assertThat(entity.getFullName()).isEqualTo("Ali Farhadi");
    assertThat(entity.getBio()).isEqualTo("CEO of AI at Allen School");
    assertThat(entity.getGraduationYear()).isEqualTo(2006);
    assertThat(entity.getBadges()).containsExactly("Apple");
    assertThat(entity.getCreatedAt()).isNull(); // Not yet persisted
    assertThat(entity.getUpdatedAt()).isNull();
  }

  @Test
  void onCreate_ShouldSetTimestamps() {
    UserProfileEntity entity = new UserProfileEntity();
    entity.onCreate();

    assertThat(entity.getCreatedAt()).isNotNull();
    assertThat(entity.getUpdatedAt()).isNotNull();
    assertThat(entity.getCreatedAt()).isBeforeOrEqualTo(Instant.now());
    assertThat(entity.getUpdatedAt()).isBeforeOrEqualTo(Instant.now());
  }

  @Test
  void onUpdate_ShouldUpdateTimestamp() throws InterruptedException {
    UserProfileEntity entity = new UserProfileEntity();
    entity.onCreate();
    Instant firstUpdate = entity.getUpdatedAt();

    Thread.sleep(10); // Ensure timestamp difference
    entity.onUpdate();

    assertThat(entity.getUpdatedAt()).isAfter(firstUpdate);
  }
}
