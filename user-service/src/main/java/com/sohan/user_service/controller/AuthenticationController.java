package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.AuthenticationRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.AuthenticationResponse;
import com.sohan.user_service.dto.response.RefreshTokenResponse;
import com.sohan.user_service.service.IAuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    IAuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            return ApiResponse.<AuthenticationResponse>builder()
                    .success(true)
                    .message("User logged in successfully")
                    .result(authenticationService.authenticate(request))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<AuthenticationResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping("refresh-token")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        try {
            return ApiResponse.<RefreshTokenResponse>builder()
                   .success(true)
                   .message("Token refreshed successfully")
                   .result(authenticationService.refreshToken(refreshToken))
                   .build();
        } catch (Exception e) {
            return ApiResponse.<RefreshTokenResponse>builder()
                   .success(false)
                   .message(e.getMessage())
                   .build();
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            log.info("Token received: {}", token);
            if (token.startsWith("Bearer "))
                token = token.substring(7);

            if (authenticationService.validateToken(token))
                return ResponseEntity.ok().build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Error validating token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
