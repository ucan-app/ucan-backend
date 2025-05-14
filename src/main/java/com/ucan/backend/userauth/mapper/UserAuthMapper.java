package com.ucan.backend.userauth.mapper;

import com.ucan.backend.userauth.BadgeDTO;
import com.ucan.backend.userauth.UserAuthDTO;
import com.ucan.backend.userauth.model.BadgeEntity;
import com.ucan.backend.userauth.model.BadgeId;
import com.ucan.backend.userauth.model.UserAuthEntity;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserAuthMapper {

  public UserAuthDTO toDTO(UserAuthEntity entity) {
    return new UserAuthDTO(
        entity.getId(),
        entity.getUsername(),
        entity.getEmail(),
        entity.getPassword(),
        entity.isEnabled(),
        entity.getBadges().stream()
            .map(badge -> new BadgeDTO(badge.getId().getOrganizationName(), badge.isValidated()))
            .collect(Collectors.toList()));
  }

  public UserAuthEntity toEntity(UserAuthDTO dto) {
    UserAuthEntity entity =
        UserAuthEntity.builder()
            .id(dto.id())
            .username(dto.username())
            .email(dto.email())
            .password(dto.password())
            .enabled(dto.enabled())
            .build();

    if (dto.badges() != null) {
      dto.badges()
          .forEach(
              badgeDTO -> {
                BadgeEntity badge =
                    BadgeEntity.builder()
                        .id(
                            BadgeId.builder()
                                .userId(entity.getId())
                                .organizationName(badgeDTO.organizationName())
                                .build())
                        .validated(badgeDTO.validated())
                        .user(entity)
                        .build();
                entity.getBadges().add(badge);
              });
    }

    return entity;
  }
}
