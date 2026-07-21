package com.swyp.voiceshield.learning;

import com.swyp.voiceshield.casecatalog.CaseScenarioRepository;
import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LearningOverviewService {

    private final LearningHistoryRepository learningHistoryRepository;
    private final CaseScenarioRepository caseScenarioRepository;

    public LearningOverviewService(
            LearningHistoryRepository learningHistoryRepository,
            CaseScenarioRepository caseScenarioRepository
    ) {
        this.learningHistoryRepository = learningHistoryRepository;
        this.caseScenarioRepository = caseScenarioRepository;
    }

    public LearningOverviewResponse getOverview(String userId) {
        String normalizedUserId = normalizeUserId(userId);
        long completedScenarioCount = learningHistoryRepository.countByUserIdAndStatus(
                normalizedUserId,
                LearningStatus.COMPLETE
        );
        long totalScenarioCount = caseScenarioRepository.count();
        List<LearningOverviewResponse.RecentLearningItem> recentLearning = learningHistoryRepository
                .findAllByUserIdOrderByUpdatedAtDescIdDesc(normalizedUserId)
                .stream()
                .map(LearningOverviewResponse.RecentLearningItem::from)
                .toList();

        return LearningOverviewResponse.from(completedScenarioCount, totalScenarioCount, recentLearning);
    }

    private String normalizeUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ApiException(ErrorCode.AUTH_REQUIRED);
        }
        return userId.trim();
    }
}
