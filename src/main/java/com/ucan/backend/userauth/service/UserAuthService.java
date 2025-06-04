package com.ucan.backend.userauth.service;

import com.ucan.backend.userauth.*;
import com.ucan.backend.userauth.mapper.UserAuthMapper;
import com.ucan.backend.userauth.model.BadgeEntity;
import com.ucan.backend.userauth.model.BadgeId;
import com.ucan.backend.userauth.model.UserAuthEntity;
import com.ucan.backend.userauth.repository.UserAuthRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService implements UserAuthAPI {

  private final UserAuthRepository userAuthRepository;
  private final UserAuthMapper userAuthMapper;
  private final PasswordEncoder passwordEncoder;
  private final ApplicationEventPublisher eventPublisher;
  private final EmailService emailService;
  private final TokenService tokenService;

  @Override
  public UserAuthDTO createUser(UserAuthDTO userDTO) {
    if (existsByUsername(userDTO.username())) {
      throw new IllegalArgumentException("Username already exists");
    }
    if (existsByEmail(userDTO.email())) {
      throw new IllegalArgumentException("Email already exists");
    }

    UserAuthEntity entity = userAuthMapper.toEntity(userDTO);
    entity.setPassword(passwordEncoder.encode(userDTO.password()));
    entity.setEnabled(false);

    UserAuthEntity savedEntity = userAuthRepository.save(entity);
    UserAuthDTO savedDTO = userAuthMapper.toDTO(savedEntity);

    try {
      String token = tokenService.generateToken(userDTO.email(), "USER");
      emailService.sendVerificationEmail(userDTO.email(), token, "USER");
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send verification email", e);
    }

    eventPublisher.publishEvent(
        new NewUserCreatedEvent(
            savedDTO.id(), savedDTO.username(), savedDTO.email(), savedEntity.getCreatedAt()));

    return savedDTO;
  }

  @Override
  public void addBadge(Long userId, BadgeDTO badgeDTO) {
    UserAuthEntity user =
        userAuthRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (userAuthRepository.existsBadgeByUserIdAndOrganization(
        userId, badgeDTO.organizationName())) {
      throw new IllegalArgumentException("Badge already exists for this user");
    }

    BadgeEntity badge =
        BadgeEntity.builder()
            .id(
                BadgeId.builder()
                    .userId(userId)
                    .organizationName(badgeDTO.organizationName())
                    .build())
            .validated(false)
            .user(user)
            .build();

    user.getBadges().add(badge);
    userAuthRepository.save(user);

    try {
      String token = tokenService.generateToken(badgeDTO.organizationName(), "BADGE");
      emailService.sendVerificationEmail(badgeDTO.organizationName(), token, "BADGE");
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send verification email", e);
    }
  }

  @Override
  public void verifyUser(String token) {
    TokenService.TokenData tokenData = tokenService.validateAndRemoveToken(token);
    if (tokenData == null || !tokenData.type().equals("USER")) {
      throw new IllegalArgumentException("Invalid or expired token");
    }

    UserAuthEntity user =
        userAuthRepository
            .findByEmail(tokenData.email())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    user.setEnabled(true);
    userAuthRepository.save(user);
  }

  @Override
  public void verifyBadge(String token) {
    TokenService.TokenData tokenData = tokenService.validateAndRemoveToken(token);
    if (tokenData == null || !tokenData.type().equals("BADGE")) {
      throw new IllegalArgumentException("Invalid or expired token");
    }

    BadgeEntity badge =
        userAuthRepository
            .findBadgeByOrganizationName(tokenData.email())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No unverified badge found for this organization"));

    badge.setValidated(true);
    userAuthRepository.save(badge.getUser());
  }

  @Override
  public void removeBadge(Long userId, BadgeDTO badgeDTO) {
    BadgeEntity badge =
        userAuthRepository
            .findBadgeByUserIdAndOrganization(userId, badgeDTO.organizationName())
            .orElseThrow(() -> new IllegalArgumentException("Badge not found"));

    if (badge.isValidated()) {
      throw new IllegalStateException("Cannot remove a validated badge");
    }

    UserAuthEntity user = badge.getUser();
    user.getBadges().remove(badge);
    userAuthRepository.save(user);
  }

  @Override
  public UserAuthDTO findByUsername(String username) {
    return userAuthRepository
        .findByUsername(username)
        .map(userAuthMapper::toDTO)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Override
  public UserAuthDTO findByEmail(String email) {
    return userAuthRepository
        .findByEmail(email)
        .map(userAuthMapper::toDTO)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Override
  public boolean existsByUsername(String username) {
    return userAuthRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userAuthRepository.existsByEmail(email);
  }
}
