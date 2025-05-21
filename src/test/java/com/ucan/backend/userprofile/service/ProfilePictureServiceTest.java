package com.ucan.backend.userprofile.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.amazonaws.services.s3.AmazonS3;
import com.ucan.backend.userprofile.model.ProfilePicture;
import com.ucan.backend.userprofile.repo.ProfilePictureRepository;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ProfilePictureServiceTest {

  @Mock private S3Service s3Service;

  @Mock private ProfilePictureRepository profilePictureRepository;

  @Mock private AmazonS3 amazonS3;

  private ProfilePictureService profilePictureService;

  @BeforeEach
  void setUp() {
    profilePictureService = new ProfilePictureService(s3Service, profilePictureRepository);
  }

  @Test
  void uploadProfilePicture_NewUser_Success() throws IOException {
    // Arrange
    String userId = "testUser";
    String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile-pictures/testUser/image.jpg";
    MultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

    when(s3Service.uploadProfilePicture(any(), any())).thenReturn(expectedUrl);
    when(profilePictureRepository.findById(userId)).thenReturn(Optional.empty());
    when(profilePictureRepository.save(any(ProfilePicture.class)))
        .thenAnswer(i -> i.getArgument(0));

    // Act
    String result = profilePictureService.uploadProfilePicture(file, userId);

    // Assert
    assertEquals(expectedUrl, result);
    verify(profilePictureRepository).save(any(ProfilePicture.class));
  }

  @Test
  void uploadProfilePicture_ExistingUser_UpdatesExistingRecord() throws IOException {
    // Arrange
    String userId = "testUser";
    String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile-pictures/testUser/image.jpg";
    MultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

    ProfilePicture existingPicture = new ProfilePicture();
    existingPicture.setUserId(userId);
    existingPicture.setPictureUrl("old-url");

    when(s3Service.uploadProfilePicture(any(), any())).thenReturn(expectedUrl);
    when(profilePictureRepository.findById(userId)).thenReturn(Optional.of(existingPicture));
    when(profilePictureRepository.save(any(ProfilePicture.class)))
        .thenAnswer(i -> i.getArgument(0));

    // Act
    String result = profilePictureService.uploadProfilePicture(file, userId);

    // Assert
    assertEquals(expectedUrl, result);
    verify(profilePictureRepository).save(any(ProfilePicture.class));
  }

  @Test
  void getProfilePictureUrl_ExistingUser_ReturnsUrl() {
    // Arrange
    String userId = "testUser";
    String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile-pictures/testUser/image.jpg";
    ProfilePicture profilePicture = new ProfilePicture();
    profilePicture.setUserId(userId);
    profilePicture.setPictureUrl(expectedUrl);

    when(profilePictureRepository.findById(userId)).thenReturn(Optional.of(profilePicture));

    // Act
    String result = profilePictureService.getProfilePictureUrl(userId);

    // Assert
    assertEquals(expectedUrl, result);
  }

  @Test
  void getProfilePictureUrl_NonExistingUser_ReturnsNull() {
    // Arrange
    String userId = "nonExistingUser";
    when(profilePictureRepository.findById(userId)).thenReturn(Optional.empty());

    // Act
    String result = profilePictureService.getProfilePictureUrl(userId);

    // Assert
    assertNull(result);
  }
}
