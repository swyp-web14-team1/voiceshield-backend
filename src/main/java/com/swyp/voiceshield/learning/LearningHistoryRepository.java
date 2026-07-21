package com.swyp.voiceshield.learning;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningHistoryRepository extends JpaRepository<LearningHistory, Long> {

    Optional<LearningHistory> findByUserIdAndScenario_Id(String userId, String scenarioId);

    long countByUserIdAndStatus(String userId, LearningStatus status);

    List<LearningHistory> findAllByUserIdOrderByUpdatedAtDescIdDesc(String userId);
}
