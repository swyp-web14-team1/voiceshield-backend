package com.swyp.voiceshield.learning;

import com.swyp.voiceshield.casecatalog.CaseScenario;
import com.swyp.voiceshield.casecatalog.CaseScenarioRepository;
import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LearningCompletionService {

    private final LearningHistoryRepository learningHistoryRepository;
    private final CaseScenarioRepository caseScenarioRepository;

    public LearningCompletionService(
            LearningHistoryRepository learningHistoryRepository,
            CaseScenarioRepository caseScenarioRepository
    ) {
        this.learningHistoryRepository = learningHistoryRepository;
        this.caseScenarioRepository = caseScenarioRepository;
    }

    @Transactional
    public LearningCompletionResponse markSimulationComplete(String userId, String scenarioId) {
        String normalizedUserId = normalizeUserId(userId);
        CaseScenario scenario = findScenario(scenarioId);
        LocalDateTime now = LocalDateTime.now();

        LearningHistory history = learningHistoryRepository.findByUserIdAndScenario_Id(normalizedUserId, scenarioId)
                .orElseGet(() -> LearningHistory.start(normalizedUserId, scenario, now));
        history.markInProgress(now);
        learningHistoryRepository.save(history);

        if (history.getStatus() == LearningStatus.COMPLETE) {
            return LearningCompletionResponse.alreadyComplete(scenarioId);
        }
        return LearningCompletionResponse.inProgress(scenarioId);
    }

    @Transactional
    public LearningCompletionResponse markQuizComplete(String userId, String scenarioId) {
        String normalizedUserId = normalizeUserId(userId);
        CaseScenario scenario = findScenario(scenarioId);
        LocalDateTime now = LocalDateTime.now();

        LearningHistory history = learningHistoryRepository.findByUserIdAndScenario_Id(normalizedUserId, scenarioId)
                .orElseGet(() -> LearningHistory.start(normalizedUserId, scenario, now));
        history.markComplete(now);
        learningHistoryRepository.save(history);

        return LearningCompletionResponse.complete(scenarioId);
    }

    private String normalizeUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ApiException(ErrorCode.AUTH_REQUIRED);
        }
        return userId.trim();
    }

    private CaseScenario findScenario(String scenarioId) {
        return caseScenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new ApiException(ErrorCode.CASE_SCENARIO_NOT_FOUND));
    }
}
