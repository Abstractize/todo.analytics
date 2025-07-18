package com.todo.analytics.model;

import java.time.OffsetDateTime;

public class DailyStat {
    private final OffsetDateTime date;
    private final int created;
    private final int completed;

    public DailyStat(OffsetDateTime date, int created, int completed) {
        this.date = date;
        this.created = created;
        this.completed = completed;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public int getCreated() {
        return created;
    }

    public int getCompleted() {
        return completed;
    }
}