package com.todo.analytics.integration;

import com.todo.analytics.model.AnalyticSummary;
import com.todo.analytics.model.DailyStat;
import com.todo.analytics.model.WeeklyAnalysis;
import com.todo.analytics.grpc.GetTasksRequest;
import com.todo.analytics.grpc.GetWeeklyTasksRequest;
import com.todo.analytics.grpc.TaskAnalyticsServiceGrpc;
import com.todo.analytics.grpc.TaskItem;
import com.todo.analytics.grpc.TaskList;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class TaskServiceClient {

    private final TaskAnalyticsServiceGrpc.TaskAnalyticsServiceFutureStub futureStub;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public TaskServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("task-service", 5000)
                .usePlaintext()
                .build();

        futureStub = TaskAnalyticsServiceGrpc.newFutureStub(channel);
    }

    public CompletableFuture<AnalyticSummary> fetchAnalyticSummary(UUID userId) {
        GetTasksRequest request = GetTasksRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        CompletableFuture<TaskList> grpcFuture = new CompletableFuture<>();

        futureStub.getUserTasks(request).addListener(() -> {
            try {
                grpcFuture.complete(futureStub.getUserTasks(request).get());
            } catch (Exception e) {
                grpcFuture.completeExceptionally(e);
            }
        }, executor);

        return grpcFuture.thenApply(grpcResponse -> {
            List<TaskItem> tasks = grpcResponse.getTasksList();

            int totalTasks = tasks.size();

            int completedTasks = (int) tasks.stream()
                    .filter(TaskItem::getIsCompleted)
                    .count();

            int completionRate = totalTasks == 0 ? 0 : (completedTasks * 100) / totalTasks;

            OffsetDateTime firstActivity = grpcResponse.getFirstActivity().isEmpty()
                    ? null
                    : OffsetDateTime.parse(grpcResponse.getFirstActivity());

            OffsetDateTime lastActivity = grpcResponse.getLastActivity().isEmpty()
                    ? null
                    : OffsetDateTime.parse(grpcResponse.getLastActivity());

            return new AnalyticSummary(
                    userId,
                    totalTasks,
                    completedTasks,
                    completionRate,
                    firstActivity,
                    lastActivity);
        });
    }

    public CompletableFuture<WeeklyAnalysis> fetchWeeklyAnalysis(UUID userId, OffsetDateTime weekStartUtc) {
        System.out.println("Fetching weekly analysis for user: " + userId + " starting from: " + weekStartUtc);

        GetWeeklyTasksRequest request = GetWeeklyTasksRequest.newBuilder()
                .setUserId(userId.toString())
                .setWeekStartDateUtc(weekStartUtc.toString())
                .build();

        CompletableFuture<TaskList> grpcFuture = new CompletableFuture<>();

        futureStub.getUserTasksOfWeek(request).addListener(() -> {
            try {
                grpcFuture.complete(futureStub.getUserTasksOfWeek(request).get());
            } catch (Exception e) {
                grpcFuture.completeExceptionally(e);
            }
        }, executor);

        return grpcFuture.thenApply(grpcResponse -> {
            List<TaskItem> tasks = grpcResponse.getTasksList();

            System.out.println("Received " + tasks.size() + " tasks for weekly analysis");

            Map<LocalDate, List<TaskItem>> grouped = tasks.stream()
                    .collect(Collectors.groupingBy(task -> OffsetDateTime.parse(task.getCreatedAt()).toLocalDate()));

            List<DailyStat> stats = grouped.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> {
                        LocalDate day = entry.getKey();
                        List<TaskItem> items = entry.getValue();

                        int total = items.size();
                        int completed = (int) items.stream().filter(TaskItem::getIsCompleted).count();

                        return new DailyStat(day.atStartOfDay().atOffset(ZoneOffset.UTC), total, completed);
                    })
                    .collect(Collectors.toList());

            return new WeeklyAnalysis(userId, stats);
        });
    }
}