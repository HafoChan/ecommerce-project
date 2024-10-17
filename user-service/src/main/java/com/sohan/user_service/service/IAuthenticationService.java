package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.AuthenticationRequest;
import com.sohan.user_service.dto.request.RefreshTokenRequest;
import com.sohan.user_service.dto.response.AuthenticationResponse;
import com.sohan.user_service.dto.response.RefreshTokenResponse;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    RefreshTokenResponse refreshToken(String request);

    boolean validateToken(String token);
}
