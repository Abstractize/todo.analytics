package com.todo.analytics.integration;

import com.todo.analytics.model.AnalyticSummary;
import com.todo.analytics.model.DailyStat;
import com.todo.analytics.model.WeeklyAnalysis;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class TaskServiceClient {

    public CompletableFuture<AnalyticSummary> fetchAnalyticSummary(UUID userId) {
        AnalyticSummary summary = new AnalyticSummary(
                userId,
                20,
                15,
                0.75,
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                OffsetDateTime.parse("2024-07-15T00:00:00Z"));
        return CompletableFuture.completedFuture(summary);
    }

    public CompletableFuture<WeeklyAnalysis> fetchWeeklyAnalysis(UUID userId, OffsetDateTime weekStartUtc) {
        List<DailyStat> stats = Arrays.asList(
                new DailyStat(weekStartUtc.plusDays(0), 3, 2),
                new DailyStat(weekStartUtc.plusDays(1), 4, 4),
                new DailyStat(weekStartUtc.plusDays(2), 2, 1),
                new DailyStat(weekStartUtc.plusDays(3), 5, 3),
                new DailyStat(weekStartUtc.plusDays(4), 3, 2),
                new DailyStat(weekStartUtc.plusDays(5), 1, 1),
                new DailyStat(weekStartUtc.plusDays(6), 2, 2));

        WeeklyAnalysis analysis = new WeeklyAnalysis(
                userId,
                stats);

        return CompletableFuture.completedFuture(analysis);
    }
}