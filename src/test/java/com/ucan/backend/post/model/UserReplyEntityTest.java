package com.ucan.backend.post.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

public class UserReplyEntityTest {

  @Test
  void testBuilder() {
    Instant now = Instant.now();
    UserReplyEntity entity =
        UserReplyEntity.builder()
            .id(1L)
            .commentId(42L)
            .authorId(100L)
            .content("Sample reply")
            .createdAt(now)
            .build();

    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getCommentId()).isEqualTo(42L);
    assertThat(entity.getAuthorId()).isEqualTo(100L);
    assertThat(entity.getContent()).isEqualTo("Sample reply");
    assertThat(entity.getCreatedAt()).isEqualTo(now);
    ;
  }

  @Test
  void testOnCreate() {
    UserReplyEntity entity = new UserReplyEntity();
    entity.onCreate();
    assertThat(entity.getCreatedAt()).isNotNull();
  }
}
