package com.ineedhousing.backend.configs;


import com.ineedhousing.backend.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigurationTest {

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpSecurity httpSecurity;

    private SecurityConfiguration securityConfiguration;

    HttpServletRequest request = new MockHttpServletRequest("GET", "/**");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfiguration = new SecurityConfiguration(authenticationProvider, jwtAuthenticationFilter);
    }

    @Test
    public void testSecurityFilterChain() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        // Mock methods to return 'httpSecurity' for method chaining
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        // Call the method under test
        SecurityFilterChain result = securityConfiguration.securityFilterChain(httpSecurity);

        // Verify that methods were called with correct parameters
        verify(httpSecurity).csrf(ArgumentMatchers.any());
        verify(httpSecurity).authorizeHttpRequests(ArgumentMatchers.any());
        verify(httpSecurity).sessionManagement(ArgumentMatchers.any());
        verify(httpSecurity).authenticationProvider(ArgumentMatchers.any());
        verify(httpSecurity).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        verify(httpSecurity).build();
    }

    @Test
    void testSecurityFilterChainThrowsException() throws Exception {
        // Arrange
        when(httpSecurity.csrf(AbstractHttpConfigurer::disable)).thenThrow(new Exception("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            securityConfiguration.securityFilterChain(httpSecurity);
        });
    }

    @Test
    void testCorsConfiguration() {
        // Act
        CorsConfiguration corsConfiguration = securityConfiguration.corsConfigurationSource().getCorsConfiguration(request);

        // Assert
        assertNotNull(corsConfiguration);
        assertTrue(corsConfiguration.getAllowedOrigins().contains("http://localhost:5173"));
        assertTrue(corsConfiguration.getAllowedHeaders().contains("*"));
        assertTrue(corsConfiguration.getAllowedMethods().contains("*"));
        assertTrue(corsConfiguration.getAllowCredentials());
    }

    @Test
    void testCorsConfigurationAllowedMethods() {
        // Act
        CorsConfiguration corsConfig = securityConfiguration.corsConfigurationSource().getCorsConfiguration(request);

        // Assert
        assertNotNull(corsConfig);
        assertEquals(1, corsConfig.getAllowedMethods().size());
        assertTrue(corsConfig.getAllowedMethods().contains("*"));
    }

    @Test
    void testCorsConfigurationAllowedHeaders() {
        // Act
        CorsConfiguration corsConfig = securityConfiguration.corsConfigurationSource().getCorsConfiguration(request);

        // Assert
        assertNotNull(corsConfig.getAllowedHeaders());
        assertEquals(1, corsConfig.getAllowedHeaders().size());
        assertTrue(corsConfig.getAllowedHeaders().contains("*"));
    }

}