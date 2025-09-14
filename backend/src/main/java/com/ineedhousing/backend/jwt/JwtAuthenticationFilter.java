package com.ineedhousing.backend.jwt;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.function.ServerRequest;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver, JwtService jwtService, UserDetailsService userDetailsService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/") || isAServiceToServiceRequest(request) || path.startsWith("/ping");
    }

    private boolean isAServiceToServiceRequest(HttpServletRequest request) {
        return request.getHeader("X-Api-Token") != null && request.getHeader("X-Service-Name") != null;
    }


    /**
     * filter requests to check if API requests are valid/contain the JWT Token
     */
    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("authenticating client request");
    try {
        // Extract JWT token from cookie instead of Authorization header
        Cookie[] cookies = request.getCookies();
        String jwt = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }
        
        // If token exists, validate it
        if (jwt != null) {
            final String userEmail = jwtService.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        }
        filterChain.doFilter(request, response);
    } catch (NullPointerException npe) {
        log.error("NPE during attempting to authenticate client, {}", npe.getMessage());
        handlerExceptionResolver.resolveException(request, response, null, npe);
    }
    catch (AuthorizationDeniedException ade) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        filterChain.doFilter(request, response);
    }
    }
}

