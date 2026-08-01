package com.swyp.voiceshield.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.swyp.voiceshield.report.RecommendationRanker.Candidate;
import com.swyp.voiceshield.report.RecommendationRanker.RankedScenario;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 추천 순위 규칙 검증. DB도 Spring 컨텍스트도 쓰지 않는다.
 */
class RecommendationRankerTest {

    private final RecommendationRanker ranker = new RecommendationRanker();
    private final VulnerabilityScorer scorer = new VulnerabilityScorer();

    private static final Map<String, Set<VulnerabilityType>> MAPPING = Map.of(
            "case-weak", Set.of(VulnerabilityType.NO_VERIFY),
            "case-middle", Set.of(VulnerabilityType.URGENCY),
            "case-strong", Set.of(VulnerabilityType.RELATION)
    );

    /** NO_VERIFY 20% / URGENCY 44% / RELATION 100% / GREED 모름 */
    private List<VulnerabilityScore> scores() {
        return scorer.scoreAll(List.of(
                new VulnerabilityCounts(VulnerabilityType.NO_VERIFY, 4, 1, 2, 0),
                new VulnerabilityCounts(VulnerabilityType.URGENCY, 3, 1, 3, 2),
                new VulnerabilityCounts(VulnerabilityType.RELATION, 3, 3, 1, 1),
                new VulnerabilityCounts(VulnerabilityType.GREED, 1, 0, 1, 0)
        ));
    }

    @Test
    @DisplayName("약한 유형을 다루는 사례를 먼저 추천한다")
    void weakestTypeFirst() {
        List<RankedScenario> ranked = ranker.rank(
                List.of(
                        new Candidate("case-strong", ScenarioLearningState.NOT_STARTED),
                        new Candidate("case-middle", ScenarioLearningState.NOT_STARTED),
                        new Candidate("case-weak", ScenarioLearningState.NOT_STARTED)),
                scores(), MAPPING);

        assertEquals(List.of("case-weak", "case-middle", "case-strong"),
                ranked.stream().map(RankedScenario::scenarioId).toList());
        assertEquals(VulnerabilityType.NO_VERIFY, ranked.get(0).reasonType());
    }

    @Test
    @DisplayName("모름인 유형만 다루는 사례는 근거 없이 뒤로 밀리되 목록에는 남는다")
    void unknownOnlyScenarioGoesLastButStays() {
        List<RankedScenario> ranked = ranker.rank(
                List.of(
                        new Candidate("case-unknown-only", ScenarioLearningState.NOT_STARTED),
                        new Candidate("case-weak", ScenarioLearningState.NOT_STARTED)),
                scores(),
                Map.of(
                        "case-unknown-only", Set.of(VulnerabilityType.GREED),
                        "case-weak", Set.of(VulnerabilityType.NO_VERIFY)));

        assertEquals(2, ranked.size());
        assertEquals("case-weak", ranked.get(0).scenarioId());
        assertEquals("case-unknown-only", ranked.get(1).scenarioId());
        assertNull(ranked.get(1).reasonType(), "모름은 추천 근거가 될 수 없다");
    }

    @Test
    @DisplayName("매핑이 없는 사례도 추천 목록에는 남는다")
    void unmappedScenarioStillRecommended() {
        List<RankedScenario> ranked = ranker.rank(
                List.of(new Candidate("case-unmapped", ScenarioLearningState.NOT_STARTED)),
                scores(), MAPPING);

        assertEquals(1, ranked.size());
        assertNull(ranked.get(0).reasonType());
    }

    @Test
    @DisplayName("조건이 같으면 진행 중인 사례를 먼저 권한다")
    void inProgressWinsTie() {
        List<RankedScenario> ranked = ranker.rank(
                List.of(
                        new Candidate("case-a-not-started", ScenarioLearningState.NOT_STARTED),
                        new Candidate("case-b-in-progress", ScenarioLearningState.IN_PROGRESS)),
                scores(),
                Map.of(
                        "case-a-not-started", Set.of(VulnerabilityType.NO_VERIFY),
                        "case-b-in-progress", Set.of(VulnerabilityType.NO_VERIFY)));

        assertEquals("case-b-in-progress", ranked.get(0).scenarioId());
    }

    @Test
    @DisplayName("한 사례가 여러 유형을 다루면 가장 약한 쪽이 추천 근거가 된다")
    void weakestCoveredTypeBecomesReason() {
        List<RankedScenario> ranked = ranker.rank(
                List.of(new Candidate("case-multi", ScenarioLearningState.NOT_STARTED)),
                scores(),
                Map.of("case-multi", Set.of(
                        VulnerabilityType.RELATION,   // 100
                        VulnerabilityType.NO_VERIFY,  // 20
                        VulnerabilityType.URGENCY))); // 44

        assertEquals(VulnerabilityType.NO_VERIFY, ranked.get(0).reasonType());
        assertEquals(20, ranked.get(0).basisScore());
    }

    @Test
    @DisplayName("최대 3개까지만 추천한다")
    void limitsToThree() {
        List<Candidate> many = List.of(
                new Candidate("case-1", ScenarioLearningState.NOT_STARTED),
                new Candidate("case-2", ScenarioLearningState.NOT_STARTED),
                new Candidate("case-3", ScenarioLearningState.NOT_STARTED),
                new Candidate("case-4", ScenarioLearningState.NOT_STARTED),
                new Candidate("case-5", ScenarioLearningState.NOT_STARTED));

        assertEquals(3, ranker.rank(many, scores(), MAPPING).size());
    }

    @Test
    @DisplayName("후보가 없으면 빈 목록이다 — 응답은 ALL_COMPLETED가 된다")
    void emptyCandidatesProduceAllCompleted() {
        List<RankedScenario> ranked = ranker.rank(List.of(), scores(), MAPPING);

        assertTrue(ranked.isEmpty());
        assertEquals(RecommendationStatus.ALL_COMPLETED,
                RecommendationResponse.of(List.of()).status());
    }
}
