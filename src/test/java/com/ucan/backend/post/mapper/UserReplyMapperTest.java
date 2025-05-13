package com.ucan.backend.post.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.post.UserReplyDTO;
import com.ucan.backend.post.model.UserReplyEntity;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class UserReplyMapperTest {

  private final UserReplyMapper mapper = new UserReplyMapper();

  @Test
  void toDTO_ShouldMapEntityToDTO() {
    UserReplyEntity entity =
        UserReplyEntity.builder()
            .id(1L)
            .commentId(42L)
            .authorId(100L)
            .content("Test reply")
            .createdAt(Instant.now())
            .build();

    UserReplyDTO dto = mapper.toDTO(entity);

    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.commentId()).isEqualTo(42L);
    assertThat(dto.authorId()).isEqualTo(100L);
    assertThat(dto.content()).isEqualTo("Test reply");
  }

  @Test
  void toEntity_ShouldMapDTOToEntity() {
    UserReplyDTO dto = new UserReplyDTO(1L, 42L, 100L, "Test reply", Instant.now());

    UserReplyEntity entity = mapper.toEntity(dto);

    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getCommentId()).isEqualTo(42L);
    assertThat(entity.getAuthorId()).isEqualTo(100L);
    assertThat(entity.getContent()).isEqualTo("Test reply");
  }
}
