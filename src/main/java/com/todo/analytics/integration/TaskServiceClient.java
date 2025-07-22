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
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import com.google.common.util.concurrent.ListenableFuture;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
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

    public CompletableFuture<AnalyticSummary> fetchAnalyticSummary(String jwt) {
        Metadata metadata = new Metadata();
        Metadata.Key<String> AUTHORIZATION_HEADER = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        metadata.put(AUTHORIZATION_HEADER, "Bearer " + jwt);

        TaskAnalyticsServiceGrpc.TaskAnalyticsServiceFutureStub stubWithHeaders = futureStub
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));

        GetTasksRequest request = GetTasksRequest.newBuilder()
                .build();

        ListenableFuture<TaskList> call = stubWithHeaders.getUserTasks(request);
        CompletableFuture<TaskList> grpcFuture = new CompletableFuture<>();

        call.addListener(() -> {
            try {
                grpcFuture.complete(call.get());
            } catch (Exception e) {
                grpcFuture.completeExceptionally(e);
            }
        }, executor);

        return grpcFuture.thenApply(grpcResponse -> {
            List<TaskItem> tasks = grpcResponse.getTasksList();
            int totalTasks = tasks.size();
            int completedTasks = (int) tasks.stream().filter(TaskItem::getIsCompleted).count();
            int completionRate = totalTasks == 0 ? 0 : (completedTasks * 100) / totalTasks;

            OffsetDateTime firstActivity = grpcResponse.getFirstActivity().isEmpty()
                    ? null
                    : OffsetDateTime.parse(grpcResponse.getFirstActivity());
            OffsetDateTime lastActivity = grpcResponse.getLastActivity().isEmpty()
                    ? null
                    : OffsetDateTime.parse(grpcResponse.getLastActivity());

            return new AnalyticSummary(totalTasks, completedTasks, completionRate, firstActivity, lastActivity);
        });
    }

    public CompletableFuture<WeeklyAnalysis> fetchWeeklyAnalysis(OffsetDateTime weekStartUtc, String jwt) {
        Metadata metadata = new Metadata();
        Metadata.Key<String> AUTHORIZATION_HEADER = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        metadata.put(AUTHORIZATION_HEADER, "Bearer " + jwt);

        TaskAnalyticsServiceGrpc.TaskAnalyticsServiceFutureStub stubWithHeaders = futureStub
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));

        GetWeeklyTasksRequest request = GetWeeklyTasksRequest.newBuilder()
                .setDayUtc(weekStartUtc.toString())
                .build();

        ListenableFuture<TaskList> call = stubWithHeaders.getUserTasksOfWeek(request);
        CompletableFuture<TaskList> grpcFuture = new CompletableFuture<>();

        call.addListener(() -> {
            try {
                grpcFuture.complete(call.get());
            } catch (Exception e) {
                grpcFuture.completeExceptionally(e);
            }
        }, executor);

        return grpcFuture.thenApply(grpcResponse -> {
            List<TaskItem> tasks = grpcResponse.getTasksList();

            Map<DayOfWeek, List<TaskItem>> grouped = tasks.stream()
                    .filter(task -> task.getCreatedAt() != null && !task.getCreatedAt().isBlank())
                    .collect(Collectors.groupingBy(task -> OffsetDateTime.parse(task.getCreatedAt()).getDayOfWeek()));

            List<DailyStat> stats = grouped.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> {
                        DayOfWeek day = entry.getKey();
                        List<TaskItem> items = entry.getValue();
                        int total = items.size();
                        int completed = (int) items.stream().filter(TaskItem::getIsCompleted).count();
                        return new DailyStat(day, total, completed);
                    })
                    .collect(Collectors.toList());

            return new WeeklyAnalysis(stats);
        });
    }
}