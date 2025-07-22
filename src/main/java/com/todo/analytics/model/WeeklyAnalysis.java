package com.todo.analytics.model;

import java.util.List;

public class WeeklyAnalysis {
    private final List<DailyStat> dailyStats;

    public WeeklyAnalysis(List<DailyStat> dailyStats) {
        this.dailyStats = dailyStats;
    }

    public List<DailyStat> getDailyStats() {
        return dailyStats;
    }
}
