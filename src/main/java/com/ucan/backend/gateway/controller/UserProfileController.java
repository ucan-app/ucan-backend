package com.ucan.backend.gateway.controller;

import com.ucan.backend.gateway.dto.userprofile.ProfileResponse;
import com.ucan.backend.userauth.UserAuthAPI;
import com.ucan.backend.userauth.UserAuthDTO;
import com.ucan.backend.userprofile.UserProfileAPI;
import com.ucan.backend.userprofile.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileAPI profileAPI;
  private final UserAuthAPI authAPI;

  @PostMapping
  public ResponseEntity<UserProfileDTO> createOrUpdate(@RequestBody UserProfileDTO dto) {
    return ResponseEntity.ok(profileAPI.createOrUpdateProfile(dto));
  }

  /**
   * Retrieves a user's profile information by their user ID. This endpoint combines profile data
   * from both the user profile and authentication services to provide a complete profile view.
   *
   * @param userId The unique identifier of the user whose profile is being requested
   * @return ResponseEntity containing a ProfileResponse with the user's complete profile
   *     information
   * @throws IllegalArgumentException if the user profile is not found
   */
  @GetMapping("/{userId}")
  public ResponseEntity<ProfileResponse> getByUserId(@PathVariable Long userId) {
    UserProfileDTO profileDTO = profileAPI.getByUserId(userId);
    UserAuthDTO authDTO = authAPI.findByUsername(profileDTO.fullName());
    ProfileResponse profileResponse =
        new ProfileResponse(
            profileDTO.userId(),
            profileDTO.fullName(),
            profileDTO.linkedinUrl(),
            profileDTO.personalWebsite(),
            profileDTO.bio(),
            profileDTO.graduationYear(),
            authDTO.badges());
    return ResponseEntity.ok(profileResponse);
  }
}
