package com.predictor.repository;

import com.predictor.entity.PredictionHistory;
import com.predictor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long> {
    List<PredictionHistory> findByUserOrderByCreatedAtDesc(User user);
    List<PredictionHistory> findByRollNumberOrderByCreatedAtDesc(String rollNumber);
    List<PredictionHistory> findTop10ByOrderByCreatedAtDesc();
    long countByRiskLevel(PredictionHistory.RiskLevel riskLevel);
}
