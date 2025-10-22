package com.ineedhousing.backend.service_interaction;

import com.ineedhousing.backend.keymaster_service.models.ApiTokenAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class ServiceApiTokenFilter extends OncePerRequestFilter {

    private final ServiceInteractionAuthenticator serviceInteractionAuthenticator;

    public ServiceApiTokenFilter(ServiceInteractionAuthenticator serviceInteractionAuthenticator) {
        this.serviceInteractionAuthenticator = serviceInteractionAuthenticator;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip this filter if already authenticated by JWT
        return request.getRequestURI().startsWith("/actuator") || isClientAuthenticated(request) ||  request.getRequestURI().startsWith("/ping")
                || request.getRequestURI().startsWith("/admin/login") ||  request.getRequestURI().startsWith("/auths") || request.getRequestURI().equals("/");

    }

    private boolean isClientAuthenticated(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null &&
                auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Request not a client, utilizing Service filter");
        String apiToken = request.getHeader("X-Api-Token");
        String serviceName = request.getHeader("X-Service-Name");

        if (!ObjectUtils.allNotNull(apiToken, serviceName) || StringUtils.isAllEmpty(apiToken, serviceName)) {
            log.warn("Missing or null X-Api-Token or X-Service-Name given in request attempted");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Authenticating service {} with API Token {}", serviceName, apiToken.substring(0,6) + "****");

        if (!serviceInteractionAuthenticator.isApiTokenAndServiceNameValid(apiToken, serviceName)) {
            log.warn("Invalid X-Api-Token or Service-Name given in request attempted");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            filterChain.doFilter(request, response);
            return;
        }

        ApiTokenAuthenticationToken authenticationToken = new ApiTokenAuthenticationToken(apiToken, serviceName);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
