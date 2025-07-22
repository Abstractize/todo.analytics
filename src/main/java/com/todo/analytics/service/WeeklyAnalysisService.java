package com.todo.analytics.service;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.todo.analytics.integration.TaskServiceClient;
import com.todo.analytics.model.WeeklyAnalysis;

@Service
public class WeeklyAnalysisService {
    private final TaskServiceClient taskServiceClient;

    @Autowired
    public WeeklyAnalysisService(TaskServiceClient taskServiceClient) {
        this.taskServiceClient = taskServiceClient;
    }

    @Async
    public CompletableFuture<WeeklyAnalysis> getWeeklyAnalytics(UUID userId, OffsetDateTime weekStartUtc, String jwt) {
        return taskServiceClient.fetchWeeklyAnalysis(userId, weekStartUtc, jwt);
    }
}