package com.ineedhousing.backend.registered_services.models;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class ApiTokenAuthenticationToken extends AbstractAuthenticationToken {

    private final String serviceName;
    private final String apiToken;

    public ApiTokenAuthenticationToken(String serviceName, String apiToken) {
        super(createAuthorities(List.of("SERVICE")));
        this.serviceName = serviceName;
        this.apiToken = apiToken;
        setAuthenticated(true);
    }

    private static Collection<? extends GrantedAuthority> createAuthorities(List<String> permissions) {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.toUpperCase()))
                .toList();
    }

    @Override
    public Object getCredentials() {
        return apiToken;
    }

    @Override
    public Object getPrincipal() {
        return serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
