package com.todo.analytics.controller;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.analytics.model.WeeklyAnalysis;
import com.todo.analytics.service.WeeklyAnalysisService;

@RestController
public class WeeklyAnalysisController {

    private final WeeklyAnalysisService weeklyAnalysisService;

    @Autowired
    public WeeklyAnalysisController(WeeklyAnalysisService weeklyAnalysisService) {
        this.weeklyAnalysisService = weeklyAnalysisService;
    }

    @GetMapping("/weekly")
    public CompletableFuture<WeeklyAnalysis> getWeeklyAnalytics(
            @RequestParam(required = true) UUID userId,
            @RequestParam(required = true) String weekStartUtc) {

        OffsetDateTime weekStart = OffsetDateTime.parse(weekStartUtc);
        return weeklyAnalysisService.getWeeklyAnalytics(userId, weekStart);
    }
}