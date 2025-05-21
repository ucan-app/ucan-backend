package com.ucan.backend.userprofile.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ucan.backend.userprofile.service.ProfilePictureService;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class ProfilePictureControllerTest {

  @Mock private ProfilePictureService profilePictureService;

  private ProfilePictureController profilePictureController;
  private UserDetails userDetails;

  @BeforeEach
  void setUp() {
    profilePictureController = new ProfilePictureController(profilePictureService);
    userDetails = new User("testUser", "password", new ArrayList<>());
  }

  @Test
  void uploadProfilePicture_Success() throws IOException {
    // Arrange
    String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile-pictures/testUser/image.jpg";
    MockMultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

    when(profilePictureService.uploadProfilePicture(any(), any())).thenReturn(expectedUrl);

    // Act
    ResponseEntity<String> response =
        profilePictureController.uploadProfilePicture(file, userDetails);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(expectedUrl, response.getBody());
    verify(profilePictureService).uploadProfilePicture(any(), eq("testUser"));
  }

  @Test
  void uploadProfilePicture_IOException_ReturnsBadRequest() throws IOException {
    // Arrange
    MockMultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

    when(profilePictureService.uploadProfilePicture(any(), any()))
        .thenThrow(new IOException("Upload failed"));

    // Act
    ResponseEntity<String> response =
        profilePictureController.uploadProfilePicture(file, userDetails);

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    assertTrue(response.getBody().contains("Failed to upload profile picture"));
  }

  @Test
  void getProfilePictureUrl_ExistingUser_ReturnsUrl() {
    // Arrange
    String userId = "testUser";
    String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile-pictures/testUser/image.jpg";
    when(profilePictureService.getProfilePictureUrl(userId)).thenReturn(expectedUrl);

    // Act
    ResponseEntity<String> response = profilePictureController.getProfilePictureUrl(userId);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(expectedUrl, response.getBody());
  }

  @Test
  void getProfilePictureUrl_NonExistingUser_ReturnsNotFound() {
    // Arrange
    String userId = "nonExistingUser";
    when(profilePictureService.getProfilePictureUrl(userId)).thenReturn(null);

    // Act
    ResponseEntity<String> response = profilePictureController.getProfilePictureUrl(userId);

    // Assert
    assertEquals(404, response.getStatusCodeValue());
  }
}
