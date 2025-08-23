package com.ineedhousing.new_listings_service.filters;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ineedhousing.new_listings_service.models.ApiTokenAuthenticationToken;
import com.ineedhousing.new_listings_service.services.ServiceRegistrationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    private final ServiceRegistrationService serviceRegistrationService;
    private final Logger logger = LoggerFactory.getLogger(ApiTokenFilter.class);

    public ApiTokenFilter(ServiceRegistrationService serviceRegistrationService) {
        this.serviceRegistrationService = serviceRegistrationService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/v1/auths/register") || path.startsWith("/actuator/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiToken = request.getHeader("X-Api-Token");
        String serviceName = request.getHeader("X-Service-Name");

        if (apiToken == null || serviceName == null) {
            logger.warn("X-Api-Token or X-Service-Name is null");
            filterChain.doFilter(request, response);
            return;
        }

        if (!serviceRegistrationService.isApiTokenAndServiceNameValid(apiToken, serviceName)) {
            logger.warn("API Token is not attached to given service: {}", serviceName);
            filterChain.doFilter(request, response);
            return;
        }

        ApiTokenAuthenticationToken authenticationToken = new ApiTokenAuthenticationToken(apiToken, serviceName);
        logger.info("Authenticating Service: {}", serviceName);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
