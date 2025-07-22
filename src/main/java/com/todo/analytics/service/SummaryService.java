package com.todo.analytics.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.todo.analytics.data.repository.AnalyticHistoryRepository;
import com.todo.analytics.integration.TaskServiceClient;
import com.todo.analytics.mapper.AnalyticMapper;
import com.todo.analytics.model.AnalyticSummary;

@Service
public class SummaryService {
    private final TaskServiceClient taskServiceClient;
    private final AnalyticHistoryRepository analyticHistoryRepository;

    @Autowired
    public SummaryService(TaskServiceClient taskServiceClient,
            AnalyticHistoryRepository analyticHistoryRepository) {
        this.taskServiceClient = taskServiceClient;
        this.analyticHistoryRepository = analyticHistoryRepository;
    }

    @Async
    public CompletableFuture<AnalyticSummary> getAnalyticSummary(UUID userId, String jwt) {
        CompletableFuture<AnalyticSummary> result = taskServiceClient.fetchAnalyticSummary(userId, jwt);

        result.thenAccept(analyticSummary -> analyticHistoryRepository.save(AnalyticMapper.toHistory(analyticSummary)));

        return result;
    }
}
