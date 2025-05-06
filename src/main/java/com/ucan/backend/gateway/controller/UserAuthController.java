package com.ucan.backend.gateway.controller;

import com.ucan.backend.gateway.dto.ErrorResponse;
import com.ucan.backend.gateway.dto.userauth.RegisterRequest;
import com.ucan.backend.userauth.UserAuthAPI;
import com.ucan.backend.userauth.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {

  private final UserAuthAPI userAuthService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      UserAuthDTO userDTO =
          new UserAuthDTO(null, request.username(), request.email(), request.password(), true);

      UserAuthDTO createdUser = userAuthService.createUser(userDTO);
      return ResponseEntity.ok(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
  }
}
