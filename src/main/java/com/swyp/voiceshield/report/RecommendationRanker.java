package com.swyp.voiceshield.report;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * 추천 순위 규칙. DB를 모르는 순수 계산이라 단독으로 테스트된다.
 *
 * <p>순서
 * <ol>
 *   <li><b>약한 유형을 다루는 사례가 먼저.</b> 사례가 다루는 유형 중 사용자 점수가 가장 낮은 것을
 *       그 사례의 우선순위로 쓴다. 여러 약점을 건드리는 사례라면 가장 약한 쪽이 기준이 된다.</li>
 *   <li>점수를 낼 수 없는(UNKNOWN) 유형은 근거로 쓰지 않는다. 모르는 것을 약하다고 볼 수 없다.
 *       근거가 하나도 없는 사례는 뒤로 밀되 목록에서 빼지는 않는다 — 안 해본 사례는 여전히 권할 만하다.</li>
 *   <li>같은 조건이면 <b>진행 중인 사례를 먼저</b> 권한다. 하다 만 것을 이어서 끝내는 편이 낫다.</li>
 *   <li>그래도 같으면 시나리오 ID 순. 응답이 호출할 때마다 흔들리지 않게 하기 위한 고정이다.</li>
 * </ol>
 */
@Component
public class RecommendationRanker {

    /** 한 번에 추천할 최대 개수. */
    public static final int MAX_RECOMMENDATIONS = 3;

    /** 근거가 없는 사례를 맨 뒤로 보내기 위한 값. 점수는 0~100이라 겹치지 않는다. */
    private static final int NO_BASIS = Integer.MAX_VALUE;

    public List<RankedScenario> rank(
            List<Candidate> candidates,
            List<VulnerabilityScore> scores,
            Map<String, Set<VulnerabilityType>> mapping
    ) {
        Map<VulnerabilityType, Integer> scoreByType = scores.stream()
                .filter(VulnerabilityScore::isScored)
                .collect(java.util.stream.Collectors.toMap(
                        VulnerabilityScore::type, VulnerabilityScore::scorePercent));

        Comparator<RankedScenario> order = Comparator
                .comparingInt(RankedScenario::basisScore)
                .thenComparing(ranked -> ranked.state() == ScenarioLearningState.IN_PROGRESS ? 0 : 1)
                .thenComparing(RankedScenario::scenarioId);

        return candidates.stream()
                .map(candidate -> toRanked(candidate, scoreByType, mapping))
                .sorted(order)
                .limit(MAX_RECOMMENDATIONS)
                .toList();
    }

    private RankedScenario toRanked(
            Candidate candidate,
            Map<VulnerabilityType, Integer> scoreByType,
            Map<String, Set<VulnerabilityType>> mapping
    ) {
        Set<VulnerabilityType> covered = mapping.getOrDefault(candidate.scenarioId(), Set.of());

        // 점수가 같으면 유형 이름으로 고정해, 같은 입력이 항상 같은 근거를 내도록 한다.
        Comparator<VulnerabilityType> weakestFirst =
                Comparator.<VulnerabilityType>comparingInt(scoreByType::get)
                        .thenComparing(VulnerabilityType::name);

        VulnerabilityType weakest = covered.stream()
                .filter(scoreByType::containsKey)
                .min(weakestFirst)
                .orElse(null);

        int basisScore = weakest == null ? NO_BASIS : scoreByType.get(weakest);
        return new RankedScenario(candidate.scenarioId(), candidate.state(), weakest, basisScore);
    }

    /** 추천 후보. 완료한 사례와 카탈로그에 노출되지 않는 사례는 여기 들어오기 전에 걸러진다. */
    public record Candidate(String scenarioId, ScenarioLearningState state) {
    }

    /**
     * 순위가 매겨진 사례.
     *
     * @param reasonType 추천 근거가 된 유형. 근거가 없으면 null
     * @param basisScore 정렬용 값. 근거가 없으면 {@link Integer#MAX_VALUE}
     */
    public record RankedScenario(
            String scenarioId,
            ScenarioLearningState state,
            VulnerabilityType reasonType,
            int basisScore
    ) {
    }
}
