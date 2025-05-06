package com.ucan.backend.userauth.mapper;

import com.ucan.backend.userauth.UserAuthDTO;
import com.ucan.backend.userauth.model.UserAuthEntity;
import org.springframework.stereotype.Component;

@Component
public class UserAuthMapper {

  public UserAuthDTO toDTO(UserAuthEntity entity) {
    return new UserAuthDTO(
        entity.getId(),
        entity.getUsername(),
        entity.getEmail(),
        entity.getPassword(),
        entity.isEnabled());
  }

  public UserAuthEntity toEntity(UserAuthDTO dto) {
    return UserAuthEntity.builder()
        .id(dto.id())
        .username(dto.username())
        .email(dto.email())
        .password(dto.password())
        .enabled(dto.enabled())
        .build();
  }
}
