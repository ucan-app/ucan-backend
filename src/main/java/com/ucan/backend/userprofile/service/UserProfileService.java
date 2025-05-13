package com.ucan.backend.userprofile.service;

import com.ucan.backend.userauth.NewUserCreatedEvent;
import com.ucan.backend.userprofile.*;
import com.ucan.backend.userprofile.mapper.UserProfileMapper;
import com.ucan.backend.userprofile.model.UserProfileEntity;
import com.ucan.backend.userprofile.repository.UserProfileRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileService implements UserProfileAPI {

  private final UserProfileRepository repository;
  private final UserProfileMapper mapper;

  @ApplicationModuleListener
  public void onNewUserCreated(NewUserCreatedEvent event) {
    UserProfileEntity entity =
        UserProfileEntity.builder().userId(event.userId()).fullName(event.username()).build();
    repository.save(entity);
  }

  @Override
  public UserProfileDTO createOrUpdateProfile(UserProfileDTO dto) {
    // try to find an existing profile for the given userId
    Optional<UserProfileEntity> optionalEntity = repository.findByUserId(dto.userId());
    UserProfileEntity entity;

    if (optionalEntity.isPresent()) {
      // Update the existing entity with new values
      entity = optionalEntity.get();
      entity.setFullName(dto.fullName());
      entity.setLinkedinUrl(dto.linkedinUrl());
      entity.setPersonalWebsite(dto.personalWebsite());
      entity.setBio(dto.bio());
      entity.setGraduationYear(dto.graduationYear());
      entity.setBadges(dto.badges());
      // No need to update createdAt
    } else {
      // No profile exists yet and create a new one using the mapper.
      entity = mapper.toEntity(dto);
    }
    return mapper.toDTO(repository.save(entity));
  }

  @Override
  public UserProfileDTO getByUserId(Long userId) {
    return repository
        .findByUserId(userId)
        .map(mapper::toDTO)
        .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
  }

  @Override
  public boolean existsByUserId(Long userId) {
    return repository.existsByUserId(userId);
  }
}
