package com.ucan.backend.userprofile.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ucan.backend.userprofile.UserProfileDTO;
import com.ucan.backend.userprofile.mapper.UserProfileMapper;
import com.ucan.backend.userprofile.model.UserProfileEntity;
import com.ucan.backend.userprofile.repository.UserProfileRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserProfileServiceTest {

  @Mock private UserProfileRepository repository;
  @Mock private UserProfileMapper mapper;

  @InjectMocks private UserProfileService service;

  private UserProfileDTO dto;
  private UserProfileEntity entity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    Instant now = Instant.now();

    dto =
        new UserProfileDTO(
            1L,
            42L,
            "Ali Farhadi",
            "https://linkedin.com/in/alifarhadi",
            "https://alifarhadi.dev",
            "Bio here",
            2025,
            List.of("UW"),
            now,
            now);

    entity =
        UserProfileEntity.builder()
            .id(1L)
            .userId(42L)
            .fullName("Ali Farhadi")
            .linkedinUrl("https://linkedin.com/in/alifarhadi")
            .personalWebsite("https://alifarhadi.dev")
            .bio("Bio here")
            .graduationYear(2025)
            .badges(List.of("UW"))
            .createdAt(now)
            .updatedAt(now)
            .build();
  }

  @Test
  void createOrUpdateProfile_ShouldReturnMappedDTO() {
    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toDTO(entity)).thenReturn(dto);

    UserProfileDTO result = service.createOrUpdateProfile(dto);

    assertNotNull(result);
    assertEquals("Ali Farhadi", result.fullName());
    verify(repository, times(1)).save(entity);
  }

  @Test
  void getByUserId_ShouldReturnMappedDTO_WhenFound() {
    when(repository.findByUserId(42L)).thenReturn(Optional.of(entity));
    when(mapper.toDTO(entity)).thenReturn(dto);

    UserProfileDTO result = service.getByUserId(42L);

    assertNotNull(result);
    assertEquals(42L, result.userId());
    verify(repository, times(1)).findByUserId(42L);
  }

  @Test
  void getByUserId_ShouldThrowException_WhenNotFound() {
    when(repository.findByUserId(404L)).thenReturn(Optional.empty());

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> service.getByUserId(404L));

    assertEquals("Profile not found for userId: 404", exception.getMessage());
    verify(repository, times(1)).findByUserId(404L);
  }

  @Test
  void existsByUserId_ShouldReturnTrue_WhenExists() {
    when(repository.existsByUserId(42L)).thenReturn(true);

    boolean result = service.existsByUserId(42L);

    assertTrue(result);
    verify(repository, times(1)).existsByUserId(42L);
  }
}
