package com.todo.analytics.controller;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.analytics.model.WeeklyAnalysis;
import com.todo.analytics.provider.IdentityProvider;
import com.todo.analytics.service.WeeklyAnalysisService;

@RestController
public class WeeklyAnalysisController {

    private final IdentityProvider identityProvider;
    private final WeeklyAnalysisService weeklyAnalysisService;

    @Autowired
    public WeeklyAnalysisController(WeeklyAnalysisService weeklyAnalysisService, IdentityProvider identityProvider) {
        this.weeklyAnalysisService = weeklyAnalysisService;
        this.identityProvider = identityProvider;
    }

    @GetMapping("/weekly")
    public CompletableFuture<WeeklyAnalysis> getWeeklyAnalytics(
            @RequestParam(required = true) String weekStartUtc) {

        UUID userId = identityProvider.getUserId().orElse(null);

        if (userId == null) {
            throw new IllegalStateException("Unauthorized user cannot access weekly analytics");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String jwt = ((JwtAuthenticationToken) auth).getToken().getTokenValue();

        OffsetDateTime weekStart = OffsetDateTime.parse(weekStartUtc);
        return weeklyAnalysisService.getWeeklyAnalytics(userId, weekStart, jwt);
    }
}