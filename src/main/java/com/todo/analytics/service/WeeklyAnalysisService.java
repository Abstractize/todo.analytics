package com.todo.analytics.service;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.todo.analytics.integration.TaskServiceClient;
import com.todo.analytics.model.WeeklyAnalysis;

@Service
public class WeeklyAnalysisService {
    private final TaskServiceClient taskServiceClient;

    @Autowired
    public WeeklyAnalysisService(TaskServiceClient taskServiceClient) {
        this.taskServiceClient = taskServiceClient;
    }

    @Async
    public CompletableFuture<WeeklyAnalysis> getWeeklyAnalytics(OffsetDateTime dayUtc, String jwt) {

        OffsetDateTime firstDayOfWeek = dayUtc.minusDays(dayUtc.getDayOfWeek().getValue() - 1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        dayUtc = firstDayOfWeek;

        return taskServiceClient.fetchWeeklyAnalysis(firstDayOfWeek, jwt);
    }
}