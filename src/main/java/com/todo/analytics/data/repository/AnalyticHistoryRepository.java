package com.todo.analytics.data.repository;

import com.todo.analytics.data.entity.AnalyticHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnalyticHistoryRepository extends JpaRepository<AnalyticHistory, UUID> {

}