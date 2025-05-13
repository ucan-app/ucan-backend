package com.ucan.backend.gateway.controller;

import com.ucan.backend.config.CustomUserDetails;
import com.ucan.backend.gateway.dto.ErrorResponse;
import com.ucan.backend.gateway.dto.userauth.LoginRequest;
import com.ucan.backend.gateway.dto.userauth.RegisterRequest;
import com.ucan.backend.userauth.UserAuthAPI;
import com.ucan.backend.userauth.UserAuthDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {

  private final UserAuthAPI userAuthService;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      UserAuthDTO userDTO =
          new UserAuthDTO(
              null,
              request.username(),
              request.email(),
              request.password(),
              true,
              new ArrayList<>());

      UserAuthDTO createdUser = userAuthService.createUser(userDTO);
      return ResponseEntity.ok(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.username(), request.password()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      HttpSession session = httpRequest.getSession(true);
      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          SecurityContextHolder.getContext());

      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      return ResponseEntity.ok(userDetails.getUserId());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ErrorResponse("Invalid username or password"));
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    SecurityContextHolder.clearContext();
    return ResponseEntity.ok().build();
  }
}
