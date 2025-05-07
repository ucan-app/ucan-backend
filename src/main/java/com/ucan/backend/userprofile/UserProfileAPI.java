package com.ucan.backend.userprofile;

public interface UserProfileAPI {
  UserProfileDTO createOrUpdateProfile(UserProfileDTO dto);

  UserProfileDTO getByUserId(Long userId);

  boolean existsByUserId(Long userId);
}
