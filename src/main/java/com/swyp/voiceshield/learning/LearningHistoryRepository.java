package com.swyp.voiceshield.learning;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningHistoryRepository extends JpaRepository<LearningHistory, Long> {

    Optional<LearningHistory> findByUserIdAndScenario_Id(String userId, String scenarioId);
}
