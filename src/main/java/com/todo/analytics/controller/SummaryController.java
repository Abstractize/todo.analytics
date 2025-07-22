package com.todo.analytics.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.analytics.model.AnalyticSummary;
import com.todo.analytics.provider.IdentityProvider;
import com.todo.analytics.service.SummaryService;

@RestController
public class SummaryController {

    private final IdentityProvider identityProvider;
    private final SummaryService analyticsService;

    @Autowired
    public SummaryController(SummaryService analyticsService, IdentityProvider identityProvider) {
        this.analyticsService = analyticsService;
        this.identityProvider = identityProvider;
    }

    @GetMapping("/summary")
    public CompletableFuture<AnalyticSummary> getAnalyticsSummary() {
        UUID userId = identityProvider.getUserId().orElse(null);

        if (userId == null) {
            throw new IllegalStateException("Unauthorized user cannot access analytics summary");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String jwt = ((JwtAuthenticationToken) auth).getToken().getTokenValue();

        return analyticsService.getAnalyticSummary(userId, jwt);
    }
}
