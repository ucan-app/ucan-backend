package com.ucan.backend.userauth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ucan.backend.userauth.BadgeDTO;
import com.ucan.backend.userauth.NewUserCreatedEvent;
import com.ucan.backend.userauth.UserAuthDTO;
import com.ucan.backend.userauth.mapper.UserAuthMapper;
import com.ucan.backend.userauth.model.BadgeEntity;
import com.ucan.backend.userauth.model.BadgeId;
import com.ucan.backend.userauth.model.UserAuthEntity;
import com.ucan.backend.userauth.repository.UserAuthRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

  @Mock private UserAuthRepository userAuthRepository;

  @Mock private UserAuthMapper userAuthMapper;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks private UserAuthService userAuthService;

  private UserAuthDTO testUserDTO;
  private UserAuthEntity testUserEntity;

  @BeforeEach
  void setUp() {
    testUserDTO =
        new UserAuthDTO(1L, "testuser", "test@example.com", "password123", true, new ArrayList<>());

    testUserEntity =
        UserAuthEntity.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("encodedPassword")
            .enabled(true)
            .createdAt(Instant.now())
            .build();
  }

  @Test
  void createUser_Success() {
    when(userAuthRepository.existsByUsername(testUserDTO.username())).thenReturn(false);
    when(userAuthRepository.existsByEmail(testUserDTO.email())).thenReturn(false);
    when(userAuthMapper.toEntity(testUserDTO)).thenReturn(testUserEntity);
    when(passwordEncoder.encode(testUserDTO.password())).thenReturn("encodedPassword");
    when(userAuthRepository.save(any(UserAuthEntity.class))).thenReturn(testUserEntity);
    when(userAuthMapper.toDTO(testUserEntity)).thenReturn(testUserDTO);

    UserAuthDTO result = userAuthService.createUser(testUserDTO);

    assertNotNull(result);
    assertEquals(testUserDTO.username(), result.username());
    assertEquals(testUserDTO.email(), result.email());
    assertTrue(result.badges().isEmpty());
    verify(eventPublisher).publishEvent(any(NewUserCreatedEvent.class));
  }

  @Test
  void createUser_WithBadges_Success() {
    // Arrange
    UserAuthDTO dtoWithBadges =
        new UserAuthDTO(
            1L,
            "testuser",
            "test@example.com",
            "password123",
            true,
            List.of(new BadgeDTO("UW", true)));

    UserAuthEntity entityWithBadges =
        UserAuthEntity.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("encodedPassword")
            .enabled(true)
            .createdAt(Instant.now())
            .build();

    BadgeEntity badge =
        BadgeEntity.builder()
            .id(BadgeId.builder().userId(1L).organizationName("UW").build())
            .validated(true)
            .user(entityWithBadges)
            .build();
    entityWithBadges.getBadges().add(badge);

    when(userAuthRepository.existsByUsername(dtoWithBadges.username())).thenReturn(false);
    when(userAuthRepository.existsByEmail(dtoWithBadges.email())).thenReturn(false);
    when(userAuthMapper.toEntity(dtoWithBadges)).thenReturn(entityWithBadges);
    when(passwordEncoder.encode(dtoWithBadges.password())).thenReturn("encodedPassword");
    when(userAuthRepository.save(any(UserAuthEntity.class))).thenReturn(entityWithBadges);
    when(userAuthMapper.toDTO(entityWithBadges)).thenReturn(dtoWithBadges);

    // Act
    UserAuthDTO result = userAuthService.createUser(dtoWithBadges);

    // Assert
    assertNotNull(result);
    assertEquals(dtoWithBadges.username(), result.username());
    assertEquals(dtoWithBadges.email(), result.email());
    assertEquals(1, result.badges().size());
    assertEquals("UW", result.badges().get(0).organizationName());
    assertTrue(result.badges().get(0).validated());
    verify(eventPublisher).publishEvent(any(NewUserCreatedEvent.class));
  }

  @Test
  void createUser_UsernameExists() {
    when(userAuthRepository.existsByUsername(testUserDTO.username())).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> userAuthService.createUser(testUserDTO));
  }

  @Test
  void createUser_EmailExists() {
    when(userAuthRepository.existsByUsername(testUserDTO.username())).thenReturn(false);
    when(userAuthRepository.existsByEmail(testUserDTO.email())).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> userAuthService.createUser(testUserDTO));
  }

  @Test
  void findByUsername_Success() {
    when(userAuthRepository.findByUsername(testUserDTO.username()))
        .thenReturn(Optional.of(testUserEntity));
    when(userAuthMapper.toDTO(testUserEntity)).thenReturn(testUserDTO);

    UserAuthDTO result = userAuthService.findByUsername(testUserDTO.username());

    assertNotNull(result);
    assertEquals(testUserDTO.username(), result.username());
  }

  @Test
  void findByUsername_NotFound() {
    when(userAuthRepository.findByUsername(testUserDTO.username())).thenReturn(Optional.empty());

    assertThrows(
        UsernameNotFoundException.class,
        () -> userAuthService.findByUsername(testUserDTO.username()));
  }

  @Test
  void findByEmail_Success() {
    when(userAuthRepository.findByEmail(testUserDTO.email()))
        .thenReturn(Optional.of(testUserEntity));
    when(userAuthMapper.toDTO(testUserEntity)).thenReturn(testUserDTO);

    UserAuthDTO result = userAuthService.findByEmail(testUserDTO.email());

    assertNotNull(result);
    assertEquals(testUserDTO.email(), result.email());
  }

  @Test
  void findByEmail_NotFound() {
    when(userAuthRepository.findByEmail(testUserDTO.email())).thenReturn(Optional.empty());

    assertThrows(
        UsernameNotFoundException.class, () -> userAuthService.findByEmail(testUserDTO.email()));
  }

  @Test
  void existsByUsername_True() {
    when(userAuthRepository.existsByUsername(testUserDTO.username())).thenReturn(true);

    boolean result = userAuthService.existsByUsername(testUserDTO.username());

    assertTrue(result);
  }

  @Test
  void existsByUsername_False() {
    when(userAuthRepository.existsByUsername(testUserDTO.username())).thenReturn(false);

    boolean result = userAuthService.existsByUsername(testUserDTO.username());

    assertFalse(result);
  }

  @Test
  void existsByEmail_True() {
    when(userAuthRepository.existsByEmail(testUserDTO.email())).thenReturn(true);

    boolean result = userAuthService.existsByEmail(testUserDTO.email());

    assertTrue(result);
  }

  @Test
  void existsByEmail_False() {
    when(userAuthRepository.existsByEmail(testUserDTO.email())).thenReturn(false);

    boolean result = userAuthService.existsByEmail(testUserDTO.email());

    assertFalse(result);
  }
}
