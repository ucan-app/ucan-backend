package com.ucan.backend.gateway.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ucan.backend.gateway.dto.userprofile.ProfileResponse;
import com.ucan.backend.userauth.BadgeDTO;
import com.ucan.backend.userauth.UserAuthAPI;
import com.ucan.backend.userauth.UserAuthDTO;
import com.ucan.backend.userprofile.UserProfileAPI;
import com.ucan.backend.userprofile.UserProfileDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

  @Mock private UserProfileAPI profileAPI;
  @Mock private UserAuthAPI authAPI;

  @InjectMocks private UserProfileController controller;

  private UserProfileDTO profileDTO;
  private UserAuthDTO authDTO;
  private ProfileResponse expectedResponse;

  @BeforeEach
  void setUp() {
    profileDTO =
        new UserProfileDTO(
            1L,
            42L,
            "John Doe",
            "https://linkedin.com/in/johndoe",
            "https://johndoe.dev",
            "Software Engineer",
            2025,
            List.of("UW"),
            null,
            null);

    authDTO =
        new UserAuthDTO(
            42L,
            "John Doe",
            "john@example.com",
            "password",
            true,
            List.of(new BadgeDTO("UW", false), new BadgeDTO("Google", false)));

    expectedResponse =
        new ProfileResponse(
            profileDTO.userId(),
            profileDTO.fullName(),
            profileDTO.linkedinUrl(),
            profileDTO.personalWebsite(),
            profileDTO.bio(),
            profileDTO.graduationYear(),
            false,
            authDTO.badges());
  }

  @Test
  void getByUserId_Success() {
    // Arrange
    when(profileAPI.getByUserId(42L)).thenReturn(profileDTO);
    when(authAPI.findByUsername(profileDTO.fullName())).thenReturn(authDTO);

    // Act
    ResponseEntity<ProfileResponse> response = controller.getByUserId(42L);

    // Assert
    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());

    ProfileResponse actualResponse = response.getBody();
    assertEquals(expectedResponse.userId(), actualResponse.userId());
    assertEquals(expectedResponse.fullName(), actualResponse.fullName());
    assertEquals(expectedResponse.linkedinUrl(), actualResponse.linkedinUrl());
    assertEquals(expectedResponse.personalWebsite(), actualResponse.personalWebsite());
    assertEquals(expectedResponse.bio(), actualResponse.bio());
    assertEquals(expectedResponse.graduationYear(), actualResponse.graduationYear());
    assertEquals(expectedResponse.badges(), actualResponse.badges());

    // Verify interactions
    verify(profileAPI).getByUserId(42L);
    verify(authAPI).findByUsername(profileDTO.fullName());
  }

  @Test
  void getByUserId_ProfileNotFound() {
    // Arrange
    when(profileAPI.getByUserId(999L)).thenThrow(new IllegalArgumentException("Profile not found"));

    // Act
    ResponseEntity<ProfileResponse> response = controller.getByUserId(999L);

    // Assert
    assertEquals(404, response.getStatusCode().value());
    assertNull(response.getBody());
    verify(profileAPI).getByUserId(999L);
    verify(authAPI, never()).findByUsername(anyString());
  }

  @Test
  void getByUserId_AuthNotFound() {
    // Arrange
    when(profileAPI.getByUserId(42L)).thenReturn(profileDTO);
    when(authAPI.findByUsername(profileDTO.fullName()))
        .thenThrow(new IllegalArgumentException("User not found"));

    // Act
    ResponseEntity<ProfileResponse> response = controller.getByUserId(42L);

    // Assert
    assertEquals(404, response.getStatusCode().value());
    assertNull(response.getBody());
    verify(profileAPI).getByUserId(42L);
    verify(authAPI).findByUsername(profileDTO.fullName());
  }
}
