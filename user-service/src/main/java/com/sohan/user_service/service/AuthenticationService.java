package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.AuthenticationRequest;
import com.sohan.user_service.dto.response.AuthenticationResponse;
import com.sohan.user_service.dto.response.RefreshTokenResponse;
import com.sohan.user_service.exception.AppException;
import com.sohan.user_service.exception.ErrorCode;
import com.sohan.user_service.security.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService{
    AuthenticationManager authenticationManager;
    JwtTokenUtil jwtTokenUtil;
    UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String token = jwtTokenUtil.generateRefreshToken(userDetails.getUsername(), userDetails.getAuthorities());
            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(String request) {
        String refreshToken = request.replace("Bearer ", "");

        if (jwtTokenUtil.extractUsername(refreshToken) == null)
            throw new AppException(ErrorCode.TOKEN_INVALID);

        if (jwtTokenUtil.isTokenExpired(refreshToken))
            throw new AppException(ErrorCode.TOKEN_EXPIRED);

        String username = jwtTokenUtil.extractUsername(refreshToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtTokenUtil.generateAccessToken(username, userDetails.getAuthorities());

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (token.startsWith("Bearer "))
                token = token.substring(7);

            String username = jwtTokenUtil.extractUsername(token);

            return jwtTokenUtil.validateTokenGateway(token, username);
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }
}
