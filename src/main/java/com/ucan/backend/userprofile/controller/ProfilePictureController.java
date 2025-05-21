package com.ucan.backend.userprofile.controller;

import com.ucan.backend.userprofile.service.ProfilePictureService;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfilePictureController {

  private final ProfilePictureService profilePictureService;

  public ProfilePictureController(ProfilePictureService profilePictureService) {
    this.profilePictureService = profilePictureService;
  }

  @PostMapping("/picture")
  public ResponseEntity<String> uploadProfilePicture(
      @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) {
    try {
      String pictureUrl =
          profilePictureService.uploadProfilePicture(file, userDetails.getUsername());
      return ResponseEntity.ok(pictureUrl);
    } catch (IOException e) {
      return ResponseEntity.badRequest()
          .body("Failed to upload profile picture: " + e.getMessage());
    }
  }

  @GetMapping("/picture/{userId}")
  public ResponseEntity<String> getProfilePictureUrl(@PathVariable String userId) {
    String pictureUrl = profilePictureService.getProfilePictureUrl(userId);
    if (pictureUrl != null) {
      return ResponseEntity.ok(pictureUrl);
    }
    return ResponseEntity.notFound().build();
  }
}
