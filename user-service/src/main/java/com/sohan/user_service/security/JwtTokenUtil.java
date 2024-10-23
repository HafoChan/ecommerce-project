package com.sohan.user_service.security;

import com.sohan.user_service.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

@Component
public class JwtTokenUtil {

    private final UserRepository userRepository;

    public JwtTokenUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    public String generateRefreshToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
        return createToken(username, expiration, authorities);
    }

    public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
        return createToken(username, expiration, authorities);
    }

    private String createToken(String subject, Date expiration, Collection<? extends GrantedAuthority> authorities) {
        List<String> permissions = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("permissions", permissions);
        claims.put("domain", "sohan.com");
        return Jwts.builder()
               .setClaims(claims)
               .setSubject(subject)
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(expiration)
               .signWith(SignatureAlgorithm.HS512, SIGNER_KEY)
               .compact();
    }

    public Boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public Boolean validateTokenGateway(String token, String username) {
        boolean checkName = userRepository.existsByUsername(username);
        return checkName && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SIGNER_KEY).parseClaimsJws(token).getBody();
    }

}
