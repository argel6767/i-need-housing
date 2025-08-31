package com.ineedhousing.backend.registered_services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ServiceApiTokenFilter extends OncePerRequestFilter {

    private final ServiceInteractionAuthenticator serviceInteractionAuthenticator;

    public ServiceApiTokenFilter(ServiceInteractionAuthenticator serviceInteractionAuthenticator) {
        this.serviceInteractionAuthenticator = serviceInteractionAuthenticator;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip this filter if already authenticated by JWT
        return request.getContextPath().startsWith("/actuator") || isClientAuthenticated(request) ||  request.getContextPath().startsWith("/ping");

    }

    private boolean isClientAuthenticated(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null &&
                auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiToken = request.getHeader("X-Api-Token");
        String serviceName = request.getHeader("X-Service-Name");

        if (ObjectUtils.allNull(apiToken, serviceName) || StringUtils.isAllEmpty(apiToken, serviceName)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!serviceInteractionAuthenticator.isApiTokenAndServiceNameValid(apiToken, serviceName)) {
            filterChain.doFilter(request, response);
            return;
        }

        ApiTokenAuthenticationToken authenticationToken = new ApiTokenAuthenticationToken(apiToken, serviceName);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
