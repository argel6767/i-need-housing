package com.ineedhousing.backend.configs;


import java.util.List;

import com.ineedhousing.backend.keymaster_service.ServiceApiTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.ineedhousing.backend.jwt.JwtAuthenticationFilter;

/**
 * Configures custom security rules for end points
 * ie what endpoints are open to everyone and which need JWT Token and set session management to
 * stateless
 * Also configures CORS for to allow for development on localhost
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration{

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ServiceApiTokenFilter serviceApiTokenFilter;

    @Value("${frontend.domain}")
    private String frontendDomain;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter, ServiceApiTokenFilter serviceApiTokenFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.serviceApiTokenFilter = serviceApiTokenFilter;
    }

    /*
     * configures the security rules of endpoints
     */
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            // Enable CSRF for cookie-based auth
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/auths/**", "/ping", "/admin/login").permitAll()
                .requestMatchers("/VAADIN/**", "/frontend/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(serviceApiTokenFilter, JwtAuthenticationFilter.class);
        
        return httpSecurity.build();
    }


    /*
     * configures allowing CORS for any local host port for testing
     */
    @Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of(frontendDomain));
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setAllowCredentials(true); // Important for cookies
    
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
    return urlBasedCorsConfigurationSource;
}

}
