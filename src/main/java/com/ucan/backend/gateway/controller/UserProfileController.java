package com.ucan.backend.gateway.controller;

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

  @PostMapping
  public ResponseEntity<UserProfileDTO> createOrUpdate(@RequestBody UserProfileDTO dto) {
    return ResponseEntity.ok(profileAPI.createOrUpdateProfile(dto));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserProfileDTO> getByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(profileAPI.getByUserId(userId));
  }
}
