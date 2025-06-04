package com.ucan.backend.userauth;

public interface UserAuthAPI {
  UserAuthDTO createUser(UserAuthDTO userDTO);

  UserAuthDTO findByUsername(String username);

  UserAuthDTO findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  void addBadge(Long userId, BadgeDTO badgeDTO);

  void removeBadge(Long userId, BadgeDTO badgeDTO);

  void verifyUser(String token);

  void verifyBadge(String token);
}
