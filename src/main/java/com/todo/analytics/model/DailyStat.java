package com.todo.analytics.model;

import java.time.DayOfWeek;

public class DailyStat {
    private final DayOfWeek dayOfWeek;
    private final int created;
    private final int completed;

    public DailyStat(DayOfWeek dayOfWeek, int created, int completed) {
        this.dayOfWeek = dayOfWeek;
        this.created = created;
        this.completed = completed;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public int getCreated() {
        return created;
    }

    public int getCompleted() {
        return completed;
    }
}