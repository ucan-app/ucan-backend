package com.ucan.backend.userauth.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.userauth.UserAuthDTO;
import com.ucan.backend.userauth.model.UserAuthEntity;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class UserAuthMapperTest {

  private final UserAuthMapper mapper = new UserAuthMapper();

  @Test
  void toDTO_ShouldMapEntityToDTO() {
    // Arrange
    UserAuthEntity entity =
        UserAuthEntity.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .enabled(true)
            .createdAt(Instant.now())
            .build();

    // Act
    UserAuthDTO dto = mapper.toDTO(entity);

    // Assert
    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.username()).isEqualTo("testuser");
    assertThat(dto.email()).isEqualTo("test@example.com");
    assertThat(dto.password()).isEqualTo("password");
    assertThat(dto.enabled()).isTrue();
  }

  @Test
  void toEntity_ShouldMapDTOToEntity() {
    // Arrange
    UserAuthDTO dto = new UserAuthDTO(1L, "testuser", "test@example.com", "password", true);

    // Act
    UserAuthEntity entity = mapper.toEntity(dto);

    // Assert
    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getUsername()).isEqualTo("testuser");
    assertThat(entity.getEmail()).isEqualTo("test@example.com");
    assertThat(entity.getPassword()).isEqualTo("password");
    assertThat(entity.isEnabled()).isTrue();
  }
}
