package com.todo.analytics.mapper;

import com.todo.analytics.data.entity.AnalyticHistory;
import com.todo.analytics.model.AnalyticSummary;

import java.time.OffsetDateTime;

public class AnalyticMapper {

    public static AnalyticHistory toHistory(AnalyticSummary summary) {
        return new AnalyticHistory(
                summary.getUserId(),
                summary.getCreatedTasks(),
                summary.getCompletedTasks(),
                summary.getCompletionRate(),
                summary.getFirstActivity(),
                summary.getLastActivity(),
                OffsetDateTime.now());
    }

    public static AnalyticSummary toSummary(AnalyticHistory history) {
        return new AnalyticSummary(
                history.getUserId(),
                history.getCreatedTasks(),
                history.getCompletedTasks(),
                history.getCompletionRate(),
                history.getFirstActivity(),
                history.getLastActivity());
    }
}
