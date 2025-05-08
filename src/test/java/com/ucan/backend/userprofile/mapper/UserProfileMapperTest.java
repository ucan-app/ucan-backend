package com.ucan.backend.userprofile.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ucan.backend.userprofile.UserProfileDTO;
import com.ucan.backend.userprofile.model.UserProfileEntity;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserProfileMapperTest {

  private final UserProfileMapper mapper = new UserProfileMapper();

  @Test
  void toDTO_ShouldMapEntityToDTO() {
    Instant now = Instant.now();

    UserProfileEntity entity =
        UserProfileEntity.builder()
            .id(1L)
            .userId(100L)
            .fullName("Iman Nilforoush")
            .linkedinUrl("https://www.linkedin.com/in/iman-nilforoush/")
            .personalWebsite("https://iman_nilforoush.dev")
            .bio("Software Developer")
            .graduationYear(2026)
            .badges(List.of("UW", "Google"))
            .createdAt(now)
            .updatedAt(now)
            .build();

    UserProfileDTO dto = mapper.toDTO(entity);

    assertThat(dto.id()).isEqualTo(entity.getId());
    assertThat(dto.userId()).isEqualTo(entity.getUserId());
    assertThat(dto.fullName()).isEqualTo(entity.getFullName());
    assertThat(dto.linkedinUrl()).isEqualTo(entity.getLinkedinUrl());
    assertThat(dto.personalWebsite()).isEqualTo(entity.getPersonalWebsite());
    assertThat(dto.bio()).isEqualTo(entity.getBio());
    assertThat(dto.graduationYear()).isEqualTo(entity.getGraduationYear());
    assertThat(dto.badges()).isEqualTo(entity.getBadges());
    assertThat(dto.createdAt()).isEqualTo(entity.getCreatedAt());
    assertThat(dto.updatedAt()).isEqualTo(entity.getUpdatedAt());
  }

  @Test
  void toEntity_ShouldMapDTOToEntity() {
    Instant now = Instant.now();

    UserProfileDTO dto =
        new UserProfileDTO(
            1L,
            100L,
            "Zohar Le",
            "https://linkedin.com/in/zohar-le",
            "https://zoharle.dev",
            "Software Developer",
            2025,
            List.of("Google", "UW"),
            now,
            now);

    UserProfileEntity entity = mapper.toEntity(dto);

    assertThat(entity.getId()).isEqualTo(dto.id());
    assertThat(entity.getUserId()).isEqualTo(dto.userId());
    assertThat(entity.getFullName()).isEqualTo(dto.fullName());
    assertThat(entity.getLinkedinUrl()).isEqualTo(dto.linkedinUrl());
    assertThat(entity.getPersonalWebsite()).isEqualTo(dto.personalWebsite());
    assertThat(entity.getBio()).isEqualTo(dto.bio());
    assertThat(entity.getGraduationYear()).isEqualTo(dto.graduationYear());
    assertThat(entity.getBadges()).isEqualTo(dto.badges());
  }
}
