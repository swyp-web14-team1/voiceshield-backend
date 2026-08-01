package com.swyp.voiceshield.report;

import com.swyp.voiceshield.casecatalog.CaseScenario;
import com.swyp.voiceshield.casecatalog.CaseScenarioRepository;
import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import com.swyp.voiceshield.learning.LearningHistory;
import com.swyp.voiceshield.learning.LearningStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 다음에 학습할 사례 추천.
 *
 * <p>후보에서 빼는 두 가지
 * <ul>
 *   <li><b>이미 완료한 사례</b> — 끝낸 것을 다시 권하지 않는다.</li>
 *   <li><b>카테고리가 없는 사례</b> — {@code category_id}가 NULL인 사례는 카탈로그에 노출되지 않는다
 *       (V17 기획 결정). 화면에서 열 수 없는 사례를 추천하면 사용자가 갈 곳이 없다.</li>
 * </ul>
 */
@Service
public class RecommendationService {

    private final CaseScenarioRepository caseScenarioRepository;
    private final LearnedScenarioRepository learnedScenarioRepository;
    private final VulnerabilityCountsProvider countsProvider;
    private final ScenarioTypeMappingProvider mappingProvider;
    private final VulnerabilityScorer scorer;
    private final RecommendationRanker ranker;

    public RecommendationService(
            CaseScenarioRepository caseScenarioRepository,
            LearnedScenarioRepository learnedScenarioRepository,
            VulnerabilityCountsProvider countsProvider,
            ScenarioTypeMappingProvider mappingProvider,
            VulnerabilityScorer scorer,
            RecommendationRanker ranker
    ) {
        this.caseScenarioRepository = caseScenarioRepository;
        this.learnedScenarioRepository = learnedScenarioRepository;
        this.countsProvider = countsProvider;
        this.mappingProvider = mappingProvider;
        this.scorer = scorer;
        this.ranker = ranker;
    }

    @Transactional(readOnly = true)
    public RecommendationResponse getRecommendations(String userId) {
        String normalizedUserId = normalizeUserId(userId);

        Map<String, LearningStatus> statusByScenarioId = learningStatusOf(normalizedUserId);
        Map<String, CaseScenario> candidateScenarios = candidateScenariosOf(statusByScenarioId);

        List<RecommendationRanker.Candidate> candidates = candidateScenarios.keySet().stream()
                .map(scenarioId -> new RecommendationRanker.Candidate(
                        scenarioId, stateOf(statusByScenarioId.get(scenarioId))))
                .toList();

        List<VulnerabilityScore> scores = scorer.scoreAll(countsProvider.getCounts(normalizedUserId));
        List<RecommendationRanker.RankedScenario> ranked =
                ranker.rank(candidates, scores, mappingProvider.getMapping());

        return RecommendationResponse.of(ranked.stream()
                .map(item -> toItem(item, candidateScenarios.get(item.scenarioId())))
                .toList());
    }

    private Map<String, LearningStatus> learningStatusOf(String userId) {
        Map<String, LearningStatus> statuses = new HashMap<>();
        for (LearningHistory history : learnedScenarioRepository.findAllByUserId(userId)) {
            statuses.put(history.getScenario().getId(), history.getStatus());
        }
        return statuses;
    }

    /** 완료했거나 카탈로그에 노출되지 않는 사례를 걸러낸 후보 목록. */
    private Map<String, CaseScenario> candidateScenariosOf(Map<String, LearningStatus> statusByScenarioId) {
        Map<String, CaseScenario> candidates = new HashMap<>();
        for (CaseScenario scenario : caseScenarioRepository.findAll()) {
            if (scenario.getCategory() == null) {
                continue;
            }
            if (statusByScenarioId.get(scenario.getId()) == LearningStatus.COMPLETE) {
                continue;
            }
            candidates.put(scenario.getId(), scenario);
        }
        return candidates;
    }

    private ScenarioLearningState stateOf(LearningStatus status) {
        return status == LearningStatus.IN_PROGRESS
                ? ScenarioLearningState.IN_PROGRESS
                : ScenarioLearningState.NOT_STARTED;
    }

    private RecommendationResponse.RecommendationItem toItem(
            RecommendationRanker.RankedScenario ranked,
            CaseScenario scenario
    ) {
        VulnerabilityType reasonType = ranked.reasonType();
        return new RecommendationResponse.RecommendationItem(
                scenario.getId(),
                scenario.getName(),
                scenario.getDifficulty(),
                scenario.getEstimatedLearningTime(),
                scenario.getCategory().getId(),
                scenario.getCategory().getName(),
                ranked.state(),
                reasonType == null ? null : reasonType.name(),
                reasonType == null ? null : reasonType.getLabel(),
                VulnerabilityMessages.recommendationReason(reasonType)
        );
    }

    private String normalizeUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ApiException(ErrorCode.AUTH_REQUIRED);
        }
        return userId.trim();
    }
}
