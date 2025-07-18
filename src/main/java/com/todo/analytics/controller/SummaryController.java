package com.todo.analytics.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.analytics.model.AnalyticSummary;
import com.todo.analytics.service.SummaryService;

@RestController
public class SummaryController {

    private final SummaryService analyticsService;

    @Autowired
    public SummaryController(SummaryService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public CompletableFuture<AnalyticSummary> getAnalyticsSummary(@RequestParam(required = true) UUID userId) {
        return analyticsService.getAnalyticSummary(userId);
    }
}
