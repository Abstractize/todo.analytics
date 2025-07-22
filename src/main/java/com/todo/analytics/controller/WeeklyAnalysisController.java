package com.todo.analytics.controller;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam(required = true) String dayUtc) {

        String jwt = identityProvider.getJwt()
                .orElseThrow(() -> new IllegalStateException("JWT token not found in the security context"));

        OffsetDateTime day = OffsetDateTime.parse(dayUtc);

        return weeklyAnalysisService.getWeeklyAnalytics(day, jwt);
    }
}