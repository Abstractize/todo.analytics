package com.todo.analytics.data.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class AnalyticHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private int createdTasks;
    private int completedTasks;
    private double completionRate;
    private OffsetDateTime firstActivity;
    private OffsetDateTime lastActivity;
    private OffsetDateTime recordedAt;

    public AnalyticHistory() {
    }

    public AnalyticHistory(UUID userId, int createdTasks, int completedTasks, double completionRate,
            OffsetDateTime firstActivity, OffsetDateTime lastActivity, OffsetDateTime recordedAt) {
        this.userId = userId;
        this.createdTasks = createdTasks;
        this.completedTasks = completedTasks;
        this.completionRate = completionRate;
        this.firstActivity = firstActivity;
        this.lastActivity = lastActivity;
        this.recordedAt = recordedAt;
    }

    public UUID getId() {
        return id;
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

    public OffsetDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setCreatedTasks(int createdTasks) {
        this.createdTasks = createdTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public void setFirstActivity(OffsetDateTime firstActivity) {
        this.firstActivity = firstActivity;
    }

    public void setLastActivity(OffsetDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public void setRecordedAt(OffsetDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

}