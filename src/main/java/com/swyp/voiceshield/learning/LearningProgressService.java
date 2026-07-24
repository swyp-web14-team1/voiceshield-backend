package com.swyp.voiceshield.learning;

import com.swyp.voiceshield.casecatalog.CaseScenarioRepository;
import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class LearningProgressService {

    private final LearningHistoryRepository learningHistoryRepository;
    private final CaseScenarioRepository caseScenarioRepository;

    public LearningProgressService(
            LearningHistoryRepository learningHistoryRepository,
            CaseScenarioRepository caseScenarioRepository
    ) {
        this.learningHistoryRepository = learningHistoryRepository;
        this.caseScenarioRepository = caseScenarioRepository;
    }

    public LearningProgressResponse getProgress(String userId) {
        String normalizedUserId = normalizeUserId(userId);
        long completedScenarioCount = learningHistoryRepository.countByUserIdAndStatus(
                normalizedUserId,
                LearningStatus.COMPLETE
        );
        long totalScenarioCount = caseScenarioRepository.count();
        return LearningProgressResponse.from(completedScenarioCount, totalScenarioCount);
    }

    private String normalizeUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ApiException(ErrorCode.AUTH_REQUIRED);
        }
        return userId.trim();
    }
}
