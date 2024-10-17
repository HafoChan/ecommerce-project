package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.AuthenticationRequest;
import com.sohan.user_service.dto.response.AuthenticationResponse;
import com.sohan.user_service.dto.response.RefreshTokenResponse;
import com.sohan.user_service.security.JwtTokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService{
    AuthenticationManager authenticationManager;
    JwtTokenUtil jwtTokenUtil;
    UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String request) {
        String refreshToken = request.replace("Bearer ", "");

        if (jwtTokenUtil.extractUsername(refreshToken) == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token has expired");
        }

        String accessToken = jwtTokenUtil.generateAccessToken(jwtTokenUtil.extractUsername(refreshToken));

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        return jwtTokenUtil.validateTokenGateway(token, username);
    }
}
