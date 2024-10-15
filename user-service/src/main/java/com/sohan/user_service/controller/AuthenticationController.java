package com.sohan.user_service.controller;

import com.sohan.user_service.dto.request.AuthenticationRequest;
import com.sohan.user_service.dto.response.ApiResponse;
import com.sohan.user_service.dto.response.AuthenticationResponse;
import com.sohan.user_service.dto.response.RefreshTokenResponse;
import com.sohan.user_service.service.IAuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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
}
