package com.ucan.backend.userprofile.service;

import com.ucan.backend.userprofile.*;
import com.ucan.backend.userprofile.mapper.UserProfileMapper;
import com.ucan.backend.userprofile.model.UserProfileEntity;
import com.ucan.backend.userprofile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileService implements UserProfileAPI {

  private final UserProfileRepository repository;
  private final UserProfileMapper mapper;

  @Override
  public UserProfileDTO createOrUpdateProfile(UserProfileDTO dto) {
    UserProfileEntity entity = mapper.toEntity(dto);
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
