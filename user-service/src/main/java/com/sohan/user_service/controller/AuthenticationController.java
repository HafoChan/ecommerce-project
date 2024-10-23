package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.AuthenticationRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.AuthenticationResponse;
import com.sohan.user_service.dto.response.RefreshTokenResponse;
import com.sohan.user_service.exception.AppException;
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
        return ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("refresh-token")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        return ApiResponse.<RefreshTokenResponse>builder()
               .success(true)
               .result(authenticationService.refreshToken(refreshToken))
               .build();
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        boolean isValid = authenticationService.validateToken(token);

        if (isValid)
            return ResponseEntity.ok().build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
