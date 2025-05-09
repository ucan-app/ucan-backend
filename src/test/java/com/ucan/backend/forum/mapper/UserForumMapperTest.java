package com.ucan.backend.forum.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.forum.UserForumDTO;
import com.ucan.backend.forum.model.UserForumEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserForumMapperTest {

  private final UserForumMapper mapper = new UserForumMapper();

  @Test
  void toDTO_ShouldMapAllFields() {
    // Given
    UUID id = UUID.randomUUID();
    UUID creatorId = UUID.randomUUID();
    String title = "Test Forum";
    String description = "Test Description";
    LocalDateTime now = LocalDateTime.now();

    UserForumEntity entity = new UserForumEntity();
    entity.setId(id);
    entity.setTitle(title);
    entity.setDescription(description);
    entity.setCreatorId(creatorId);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    // When
    UserForumDTO dto = mapper.toDTO(entity);

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
    UUID id = UUID.randomUUID();
    UUID creatorId = UUID.randomUUID();
    String title = "Test Forum";
    String description = "Test Description";
    LocalDateTime now = LocalDateTime.now();

    UserForumDTO dto = new UserForumDTO(id, title, description, creatorId, now, now);

    // When
    UserForumEntity entity = mapper.toEntity(dto);

    // Then
    assertThat(entity.getId()).isEqualTo(id);
    assertThat(entity.getTitle()).isEqualTo(title);
    assertThat(entity.getDescription()).isEqualTo(description);
    assertThat(entity.getCreatorId()).isEqualTo(creatorId);
    assertThat(entity.getCreatedAt()).isEqualTo(now);
    assertThat(entity.getUpdatedAt()).isEqualTo(now);
  }
}
