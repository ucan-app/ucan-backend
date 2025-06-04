package com.ucan.backend.userauth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.ucan.backend.userauth.BadgeDTO;
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

  @Mock private EmailService emailService;

  @Mock private TokenService tokenService;

  @InjectMocks private UserAuthService userAuthService;

  private UserAuthDTO testUserDTO;
  private UserAuthEntity testUserEntity;
  private BadgeDTO testBadgeDTO;

  @BeforeEach
  void setUp() {
    testUserDTO =
        new UserAuthDTO(
            1L, "testuser", "test@example.com", "password123", false, new ArrayList<>());

    testUserEntity =
        UserAuthEntity.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("encodedPassword")
            .enabled(false)
            .createdAt(Instant.now())
            .build();

    testBadgeDTO = new BadgeDTO("UW", false);
  }

  @Test
  void createUser_Success() throws Exception {
    when(userAuthRepository.existsByUsername(testUserDTO.username())).thenReturn(false);
    when(userAuthRepository.existsByEmail(testUserDTO.email())).thenReturn(false);
    when(userAuthMapper.toEntity(testUserDTO)).thenReturn(testUserEntity);
    when(passwordEncoder.encode(testUserDTO.password())).thenReturn("encodedPassword");
    when(userAuthRepository.save(any(UserAuthEntity.class))).thenReturn(testUserEntity);
    when(userAuthMapper.toDTO(testUserEntity)).thenReturn(testUserDTO);
    when(tokenService.generateToken(eq(testUserDTO.email()), eq("USER"))).thenReturn("test-token");
    doNothing()
        .when(emailService)
        .sendVerificationEmail(eq(testUserDTO.email()), eq("test-token"), eq("USER"));

    UserAuthDTO result = userAuthService.createUser(testUserDTO);

    assertNotNull(result);
    assertEquals(testUserDTO.username(), result.username());
    assertEquals(testUserDTO.email(), result.email());
    assertTrue(result.badges().isEmpty());
    assertFalse(result.enabled());
    verify(tokenService).generateToken(testUserDTO.email(), "USER");
    verify(emailService).sendVerificationEmail(testUserDTO.email(), "test-token", "USER");
  }

  @Test
  void createUser_WithBadges_Success() throws Exception {
    UserAuthDTO dtoWithBadges =
        new UserAuthDTO(
            1L,
            "testuser",
            "test@example.com",
            "password123",
            false,
            List.of(new BadgeDTO("UW", true)));

    UserAuthEntity entityWithBadges =
        UserAuthEntity.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("encodedPassword")
            .enabled(false)
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
    when(tokenService.generateToken(eq(dtoWithBadges.email()), eq("USER")))
        .thenReturn("test-token");
    doNothing()
        .when(emailService)
        .sendVerificationEmail(eq(dtoWithBadges.email()), eq("test-token"), eq("USER"));

    UserAuthDTO result = userAuthService.createUser(dtoWithBadges);

    assertNotNull(result);
    assertEquals(dtoWithBadges.username(), result.username());
    assertEquals(dtoWithBadges.email(), result.email());
    assertEquals(1, result.badges().size());
    assertEquals("UW", result.badges().get(0).organizationName());
    assertTrue(result.badges().get(0).validated());
    assertFalse(result.enabled());
    verify(tokenService).generateToken(dtoWithBadges.email(), "USER");
    verify(emailService).sendVerificationEmail(dtoWithBadges.email(), "test-token", "USER");
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

  @Test
  void addBadge_Success() throws Exception {
    when(userAuthRepository.findById(1L)).thenReturn(Optional.of(testUserEntity));
    when(userAuthRepository.existsBadgeByUserIdAndOrganization(1L, "UW")).thenReturn(false);
    when(tokenService.generateToken(eq(testBadgeDTO.organizationName()), eq("BADGE")))
        .thenReturn("test-token");
    doNothing()
        .when(emailService)
        .sendVerificationEmail(eq(testBadgeDTO.organizationName()), eq("test-token"), eq("BADGE"));

    userAuthService.addBadge(1L, testBadgeDTO);

    verify(userAuthRepository).save(testUserEntity);
    assertEquals(1, testUserEntity.getBadges().size());
    assertEquals("UW", testUserEntity.getBadges().get(0).getId().getOrganizationName());
    assertFalse(testUserEntity.getBadges().get(0).isValidated());
    verify(tokenService).generateToken(testBadgeDTO.organizationName(), "BADGE");
    verify(emailService)
        .sendVerificationEmail(testBadgeDTO.organizationName(), "test-token", "BADGE");
  }

  @Test
  void addBadge_UserNotFound() {
    // Arrange
    when(userAuthRepository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> userAuthService.addBadge(1L, testBadgeDTO),
        "Should throw when user not found");
  }

  @Test
  void addBadge_BadgeAlreadyExists() {
    // Arrange
    when(userAuthRepository.findById(1L)).thenReturn(Optional.of(testUserEntity));
    when(userAuthRepository.existsBadgeByUserIdAndOrganization(1L, "UW")).thenReturn(true);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> userAuthService.addBadge(1L, testBadgeDTO),
        "Should throw when badge already exists");
  }

  @Test
  void removeBadge_Success() {
    // Arrange
    BadgeEntity badge =
        BadgeEntity.builder()
            .id(BadgeId.builder().userId(1L).organizationName("UW").build())
            .validated(false)
            .user(testUserEntity)
            .build();
    testUserEntity.getBadges().add(badge);

    when(userAuthRepository.findBadgeByUserIdAndOrganization(1L, "UW"))
        .thenReturn(Optional.of(badge));

    // Act
    userAuthService.removeBadge(1L, testBadgeDTO);

    // Assert
    verify(userAuthRepository).save(testUserEntity);
    assertTrue(testUserEntity.getBadges().isEmpty());
  }

  @Test
  void removeBadge_BadgeNotFound() {
    // Arrange
    when(userAuthRepository.findBadgeByUserIdAndOrganization(1L, "UW"))
        .thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> userAuthService.removeBadge(1L, testBadgeDTO),
        "Should throw when badge not found");
  }

  @Test
  void removeBadge_ValidatedBadge() {
    // Arrange
    BadgeEntity badge =
        BadgeEntity.builder()
            .id(BadgeId.builder().userId(1L).organizationName("UW").build())
            .validated(true)
            .user(testUserEntity)
            .build();
    testUserEntity.getBadges().add(badge);

    when(userAuthRepository.findBadgeByUserIdAndOrganization(1L, "UW"))
        .thenReturn(Optional.of(badge));

    // Act & Assert
    assertThrows(
        IllegalStateException.class,
        () -> userAuthService.removeBadge(1L, testBadgeDTO),
        "Should throw when trying to remove validated badge");
  }
}
