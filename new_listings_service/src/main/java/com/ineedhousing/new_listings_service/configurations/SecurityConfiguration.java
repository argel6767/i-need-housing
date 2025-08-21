package com.ineedhousing.new_listings_service.configurations;

import com.ineedhousing.new_listings_service.filters.ApiTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final ApiTokenFilter apiTokenFilter;

    public SecurityConfiguration(ApiTokenFilter apiTokenFilter) {
        this.apiTokenFilter = apiTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/v1/auths/register").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(apiTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
