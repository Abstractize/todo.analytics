package com.todo.analytics.provider;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class IdentityProvider {

    public Optional<UUID> getUserId() {
        return getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier")
                .map(UUID::fromString);
    }

    public Optional<String> getEmail() {
        return getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
    }

    public Optional<String> getName() {
        return getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name");
    }

    public Optional<String> getRole() {
        return getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
    }

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    private Optional<String> getClaim(String claimName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) principal;
            Object claimValue = jwt.getClaim(claimName);
            if (claimValue != null) {
                return Optional.of(claimValue.toString());
            }
        }

        return Optional.empty();
    }
}