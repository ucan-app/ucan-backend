package com.ucan.backend.userauth.service;

import com.ucan.backend.userauth.*;
import com.ucan.backend.userauth.mapper.UserAuthMapper;
import com.ucan.backend.userauth.model.UserAuthEntity;
import com.ucan.backend.userauth.repository.UserAuthRepository;
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

    UserAuthEntity savedEntity = userAuthRepository.save(entity);
    UserAuthDTO savedDTO = userAuthMapper.toDTO(savedEntity);

    eventPublisher.publishEvent(
        new NewUserCreatedEvent(
            savedDTO.id(), savedDTO.username(), savedDTO.email(), savedEntity.getCreatedAt()));

    return savedDTO;
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
