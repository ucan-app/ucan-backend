package com.ucan.backend.gateway.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ucan.backend.gateway.dto.ErrorResponse;
import com.ucan.backend.gateway.dto.userauth.RegisterRequest;
import com.ucan.backend.userauth.UserAuthAPI;
import com.ucan.backend.userauth.UserAuthDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class UserAuthControllerTest {

  @Mock private UserAuthAPI mockUserAuthService;

  @InjectMocks private UserAuthController userAuthController;

  private RegisterRequest validRegisterRequest;
  private UserAuthDTO expectedUserDTO;

  @BeforeEach
  void setUp() {
    validRegisterRequest = new RegisterRequest("testuser", "test@example.com", "password123");
    expectedUserDTO = new UserAuthDTO(1L, "testuser", "test@example.com", "encodedPassword", true);
  }

  @Test
  void
      register_whenServiceThrowsIllegalArgumentException_shouldReturnBadRequestWithErrorResponse() {
    // throw an IllegalArgumentException
    String errorMessage = "Username already exists";
    when(mockUserAuthService.createUser(any(UserAuthDTO.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    // Call the register method on the controller
    ResponseEntity<?> responseEntity = userAuthController.register(validRegisterRequest);

    // Verify the outcome
    assertNotNull(responseEntity, "Response entity should not be null");
    assertEquals(
        HttpStatus.BAD_REQUEST,
        responseEntity.getStatusCode(),
        "HTTP status should be BAD_REQUEST");

    ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
    assertNotNull(errorResponse, "Response body should not be null and contain ErrorResponse");
    assertEquals(
        errorMessage, errorResponse.message(), "Error message should match the exception message");

    // Verify that createUser was called
    verify(mockUserAuthService).createUser(any(UserAuthDTO.class));
  }
}
