package com.todo.analytics.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AnalyticSummary {
    private final UUID userId;
    private final int createdTasks;
    private final int completedTasks;
    private final double completionRate;
    private final OffsetDateTime firstActivity;
    private final OffsetDateTime lastActivity;

    public AnalyticSummary(UUID userId, int createdTasks, int completedTasks, double completionRate,
            OffsetDateTime firstActivity, OffsetDateTime lastActivity) {
        this.userId = userId;
        this.createdTasks = createdTasks;
        this.completedTasks = completedTasks;
        this.completionRate = completionRate;
        this.firstActivity = firstActivity;
        this.lastActivity = lastActivity;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getCreatedTasks() {
        return createdTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public OffsetDateTime getFirstActivity() {
        return firstActivity;
    }

    public OffsetDateTime getLastActivity() {
        return lastActivity;
    }
}