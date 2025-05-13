package com.ucan.backend.post.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.post.UserCommentDTO;
import com.ucan.backend.post.model.UserCommentEntity;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class UserCommentMapperTest {

  private final UserCommentMapper mapper = new UserCommentMapper();

  @Test
  void toDTO_ShouldMapEntityToDTO() {
    Instant now = Instant.now();
    UserCommentEntity entity =
        UserCommentEntity.builder()
            .id(1L)
            .postId(100L)
            .authorId(200L)
            .content("Test comment")
            .replyCount(2)
            .createdAt(now)
            .build();

    UserCommentDTO dto = mapper.toDTO(entity);

    assertThat(dto.id()).isEqualTo(entity.getId());
    assertThat(dto.postId()).isEqualTo(entity.getPostId());
    assertThat(dto.authorId()).isEqualTo(entity.getAuthorId());
    assertThat(dto.content()).isEqualTo(entity.getContent());
    assertThat(dto.replyCount()).isEqualTo(entity.getReplyCount());
    assertThat(dto.createdAt()).isEqualTo(entity.getCreatedAt());
  }

  @Test
  void toEntity_ShouldMapDTOToEntity() {
    Instant now = Instant.now();
    UserCommentDTO dto = new UserCommentDTO(1L, 100L, 200L, "Test comment", 2, now);

    UserCommentEntity entity = mapper.toEntity(dto);

    assertThat(entity.getId()).isEqualTo(dto.id());
    assertThat(entity.getPostId()).isEqualTo(dto.postId());
    assertThat(entity.getAuthorId()).isEqualTo(dto.authorId());
    assertThat(entity.getContent()).isEqualTo(dto.content());
    assertThat(entity.getReplyCount()).isEqualTo(dto.replyCount());
    assertThat(entity.getCreatedAt()).isEqualTo(dto.createdAt());
  }
}
