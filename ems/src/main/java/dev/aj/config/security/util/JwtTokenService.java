package dev.aj.config.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

    @Value("${app.jwt-keyid}")
    private String jwtKeyId;

    @Value("${app.jwt-secret}")
    private String secret;
    @Value("${app.jwt-expiration-minutes}")
    private int expiration;


    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        Instant jwtCreationInstant = Instant.now();
        Instant jwtExpirationInstant = Instant.now().plus(expiration, TimeUnit.MINUTES.toChronoUnit());

        return Jwts.builder()
                   .header().keyId(jwtKeyId)
                   .and()
                   .subject(username)
                   .issuedAt(Date.from(jwtCreationInstant))
                   .expiration(Date.from(jwtExpirationInstant))
                   .signWith(getSecretKey(), Jwts.SIG.HS256)
                   .compact();
    }

    public String getUsernameFromToken(String token) throws UserPrincipalNotFoundException {
        try {
            return getTokenPayload(token)
                    .getSubject();
        } catch (JwtException e) {
            throw new UserPrincipalNotFoundException("Request doesn't carry a valid JWT");
        }
    }

    public boolean validateToken(String token) {
        try {
            Instant expirationInstant = getTokenPayload(token)
                    .getExpiration()
                    .toInstant();
            return expirationInstant.isAfter(Instant.now());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getTokenPayload(String token) {
        return Jwts.parser()
                   .verifyWith(getSecretKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
