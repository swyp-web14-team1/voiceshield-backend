package com.swyp.voiceshield.learning;

import com.swyp.voiceshield.casecatalog.CaseScenario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_histories")
public class LearningHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_history_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scenario_id", nullable = false)
    private CaseScenario scenario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LearningStatus status;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected LearningHistory() {
    }

    public static LearningHistory start(String userId, CaseScenario scenario, LocalDateTime now) {
        LearningHistory history = new LearningHistory();
        history.userId = userId;
        history.scenario = scenario;
        history.status = LearningStatus.IN_PROGRESS;
        history.createdAt = now;
        history.updatedAt = now;
        return history;
    }

    public void markInProgress(LocalDateTime now) {
        if (status != LearningStatus.COMPLETE) {
            status = LearningStatus.IN_PROGRESS;
        }
        updatedAt = now;
    }

    public void markComplete(LocalDateTime now) {
        status = LearningStatus.COMPLETE;
        completedAt = now;
        updatedAt = now;
    }

    public String getUserId() {
        return userId;
    }

    public CaseScenario getScenario() {
        return scenario;
    }

    public LearningStatus getStatus() {
        return status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
