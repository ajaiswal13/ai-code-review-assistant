package com.aj.aireview.security.jwt;

import com.aj.aireview.security.user.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(AuthenticatedUser user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim(
                        "roles",
                        user.getAuthorities()
                                .stream()
                                .map(authority -> authority.getAuthority())
                                .toList()
                )
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(
                                Instant.now()
                                        .plusMillis(jwtProperties.getExpiration())
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(
            String token,
            AuthenticatedUser user
    ) {

        String username = extractUsername(token);

        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {

        Date expiration =
                extractClaim(token, Claims::getExpiration);

        return expiration.before(new Date());
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties
                        .getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

}
