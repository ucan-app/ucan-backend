package com.ucan.backend.userprofile.mapper;

import com.ucan.backend.userprofile.UserProfileDTO;
import com.ucan.backend.userprofile.model.UserProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

  public UserProfileDTO toDTO(UserProfileEntity entity) {
    return new UserProfileDTO(
        entity.getId(),
        entity.getUserId(),
        entity.getFullName(),
        entity.getLinkedinUrl(),
        entity.getPersonalWebsite(),
        entity.getBio(),
        entity.getGraduationYear(),
        entity.getBadges(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  public UserProfileEntity toEntity(UserProfileDTO dto) {
    return UserProfileEntity.builder()
        .id(dto.id())
        .userId(dto.userId())
        .fullName(dto.fullName())
        .linkedinUrl(dto.linkedinUrl())
        .personalWebsite(dto.personalWebsite())
        .bio(dto.bio())
        .graduationYear(dto.graduationYear())
        .badges(dto.badges())
        .build();
  }
}
