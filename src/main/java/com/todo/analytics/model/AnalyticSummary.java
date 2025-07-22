package com.todo.analytics.model;

import java.time.OffsetDateTime;

public class AnalyticSummary {
    private final int createdTasks;
    private final int completedTasks;
    private final double completionRate;
    private final OffsetDateTime firstActivity;
    private final OffsetDateTime lastActivity;

    public AnalyticSummary(int createdTasks, int completedTasks, double completionRate,
            OffsetDateTime firstActivity, OffsetDateTime lastActivity) {
        this.createdTasks = createdTasks;
        this.completedTasks = completedTasks;
        this.completionRate = completionRate;
        this.firstActivity = firstActivity;
        this.lastActivity = lastActivity;
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