package com.sohan.user_service.security;

import com.sohan.user_service.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

@Component
public class JwtTokenUtil {

    private final UserRepository userRepository;

    public JwtTokenUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    public String generateRefreshToken(String username) {
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
        return createToken(username, expiration);
    }

    public String generateAccessToken(String username) {
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
        return createToken(username, expiration);
    }

    private String createToken(String subject, Date expiration) {
        Map<String, Object> claims = new HashMap<String, Object>();
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
