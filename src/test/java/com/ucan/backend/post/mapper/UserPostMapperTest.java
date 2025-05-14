package com.ucan.backend.post.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.post.UserPostDTO;
import com.ucan.backend.post.model.UserPostEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UserPostMapperTest {

  private final UserPostMapper mapper = new UserPostMapper();

  @Test
  void toDTO_ShouldMapAllFields() {
    // Given
    Long id = 1L;
    Long creatorId = 2L;
    String title = "Test Forum";
    String description = "Test Description";
    LocalDateTime now = LocalDateTime.now();

    UserPostEntity entity = new UserPostEntity();
    entity.setId(id);
    entity.setTitle(title);
    entity.setDescription(description);
    entity.setCreatorId(creatorId);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    // When
    UserPostDTO dto = mapper.toDTO(entity);

    // Then
    assertThat(dto.id()).isEqualTo(id);
    assertThat(dto.title()).isEqualTo(title);
    assertThat(dto.description()).isEqualTo(description);
    assertThat(dto.creatorId()).isEqualTo(creatorId);
    assertThat(dto.createdAt()).isEqualTo(now);
    assertThat(dto.updatedAt()).isEqualTo(now);
  }

  @Test
  void toEntity_ShouldMapAllFields() {
    // Given
    Long id = 1L;
    Long creatorId = 2L;
    String title = "Test Forum";
    String description = "Test Description";
    LocalDateTime now = LocalDateTime.now();

    UserPostDTO dto = new UserPostDTO(id, title, description, creatorId, now, now);

    // When
    UserPostEntity entity = mapper.toEntity(dto);

    // Then
    assertThat(entity.getId()).isEqualTo(id);
    assertThat(entity.getTitle()).isEqualTo(title);
    assertThat(entity.getDescription()).isEqualTo(description);
    assertThat(entity.getCreatorId()).isEqualTo(creatorId);
    assertThat(entity.getCreatedAt()).isEqualTo(now);
    assertThat(entity.getUpdatedAt()).isEqualTo(now);
  }
}
