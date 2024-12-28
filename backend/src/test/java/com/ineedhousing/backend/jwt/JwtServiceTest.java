package com.ineedhousing.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final Long EXPIRATION_TIME = 86400000L; // 1 day

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "expirationTime", EXPIRATION_TIME);

        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void testExtractUsernameFromToken() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        String username = jwtService.extractUsername(token);

        // Then
        assertThat(username).isEqualTo("testUser");
    }

    @Test
    void testGenerateTokenWithoutExtraClaims() {
        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertThat(token).isNotEmpty();
        assertThat(jwtService.extractUsername(token)).isEqualTo("testUser");
    }

    @Test
    void testGenerateTokenWithExtraClaims() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        // When
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Then
        assertThat(token).isNotEmpty();
        assertThat(Optional.ofNullable(jwtService.extractClaim(token, claims -> claims.get("role")))).get()
                .isEqualTo("ADMIN");
    }

    @Test
    void testTokenValidation() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void testTokenValidationWithWrongUser() {
        // Given
        String token = jwtService.generateToken(userDetails);
        UserDetails wrongUser = mock(UserDetails.class);
        when(wrongUser.getUsername()).thenReturn("wrongUser");

        // When
        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void testTokenValidationWithExpiredToken() {
        // Given
        ReflectionTestUtils.setField(jwtService, "expirationTime", -1000L);
        String token = jwtService.generateToken(userDetails);
        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.isTokenValid(token, userDetails);
        });
    }

    @Test
    void testExtractClaimFromToken() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);

        // Then
        assertThat(issuedAt).isNotNull();
        assertThat(issuedAt).isBefore(new Date());
    }

    @Test
    void testGetExpirationTime() {
        // When
        long actualExpirationTime = jwtService.getExpirationTime();

        // Then
        assertThat(actualExpirationTime).isEqualTo(EXPIRATION_TIME);
    }
}