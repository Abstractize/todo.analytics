package com.todo.analytics.model;

import java.util.List;
import java.util.UUID;

public class WeeklyAnalysis {
    private final UUID userId;
    private final List<DailyStat> dailyStats;

    public WeeklyAnalysis(UUID userId,
            List<DailyStat> dailyStats) {
        this.userId = userId;
        this.dailyStats = dailyStats;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<DailyStat> getDailyStats() {
        return dailyStats;
    }
}
