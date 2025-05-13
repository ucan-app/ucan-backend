package com.ucan.backend.post.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

public class UserCommentEntityTest {

  @Test
  void builder_ShouldBuildEntity() {
    Instant now = Instant.now();
    UserCommentEntity entity =
        UserCommentEntity.builder()
            .id(1L)
            .postId(100L)
            .authorId(200L)
            .content("Sample comment")
            .replyCount(3)
            .createdAt(now)
            .build();

    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getPostId()).isEqualTo(100L);
    assertThat(entity.getAuthorId()).isEqualTo(200L);
    assertThat(entity.getContent()).isEqualTo("Sample comment");
    assertThat(entity.getReplyCount()).isEqualTo(3);
    assertThat(entity.getCreatedAt()).isEqualTo(now);
  }
}
