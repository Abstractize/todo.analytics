package com.todo.analytics.mapper;

import com.todo.analytics.data.entity.AnalyticHistory;
import com.todo.analytics.model.AnalyticSummary;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AnalyticMapper {

    public static AnalyticHistory toHistory(AnalyticSummary summary, UUID userId) {
        return new AnalyticHistory(
                userId,
                summary.getCreatedTasks(),
                summary.getCompletedTasks(),
                summary.getCompletionRate(),
                summary.getFirstActivity(),
                summary.getLastActivity(),
                OffsetDateTime.now());
    }

    public static AnalyticSummary toSummary(AnalyticHistory history) {
        return new AnalyticSummary(
                history.getCreatedTasks(),
                history.getCompletedTasks(),
                history.getCompletionRate(),
                history.getFirstActivity(),
                history.getLastActivity());
    }
}
