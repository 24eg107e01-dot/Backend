package com.predictor.repository;

import com.predictor.entity.AcademicAlert;
import com.predictor.entity.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AcademicAlertRepository extends JpaRepository<AcademicAlert, Long> {
    List<AcademicAlert> findAllByOrderByCreatedAtDesc();
    List<AcademicAlert> findByStatusOrderByCreatedAtDesc(AcademicAlert.AlertStatus status);
    List<AcademicAlert> findByRiskLevelOrderByCreatedAtDesc(PredictionHistory.RiskLevel riskLevel);
    long countByStatus(AcademicAlert.AlertStatus status);
}
