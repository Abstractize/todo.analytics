package com.todo.analytics.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

        UUID userId = identityProvider.getUserId()
                .orElseThrow(() -> new IllegalStateException("User ID not found in the security context"));
        String jwt = identityProvider.getJwt()
                .orElseThrow(() -> new IllegalStateException("JWT token not found in the security context"));

        return analyticsService.getAnalyticSummary(userId, jwt);
    }
}
