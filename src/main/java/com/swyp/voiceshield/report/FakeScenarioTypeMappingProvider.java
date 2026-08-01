package com.swyp.voiceshield.report;

import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * A 완성 전까지 쓰는 잠정 매핑. <b>A가 선택지 태깅을 끝내면 이 클래스를 삭제한다.</b>
 *
 * <p>값은 정본(swyp/시나리오/*.md)의 수법 서술을 읽고 임시로 채운 것이다.
 * <b>확정된 매핑이 아니다</b> — 실제 태깅 결과와 다를 수 있으므로 화면에 나가는 추천 사유를
 * 이 값으로 검수하지 말 것.
 */
@Component
public class FakeScenarioTypeMappingProvider implements ScenarioTypeMappingProvider {

    @Override
    public Map<String, Set<VulnerabilityType>> getMapping() {
        return Map.of(
                // 가족이 새 번호로 연락해 급히 송금을 요구하고, 통화 확인을 피한다
                "case-mobile-repair", Set.of(
                        VulnerabilityType.RELATION,
                        VulnerabilityType.URGENCY,
                        VulnerabilityType.NO_VERIFY),

                // 택배사를 사칭해 주소 오류를 알리며 링크 입력을 재촉한다
                "case-return-delivery", Set.of(
                        VulnerabilityType.AUTHORITY,
                        VulnerabilityType.URGENCY),

                // 소방기관을 사칭해 과태료·점검을 근거로 압박한다
                "case-fire-agency", Set.of(
                        VulnerabilityType.AUTHORITY,
                        VulnerabilityType.FEAR,
                        VulnerabilityType.URGENCY),

                // 원금 보장·기간 한정으로 이익을 앞세워 유인한다
                "case-special-investment", Set.of(
                        VulnerabilityType.GREED,
                        VulnerabilityType.URGENCY,
                        VulnerabilityType.NO_VERIFY),

                // 가족을 사칭하되 통화를 거부하고 메신저로만 송금을 요구한다
                "case-new-number-family-transfer", Set.of(
                        VulnerabilityType.RELATION,
                        VulnerabilityType.NO_VERIFY,
                        VulnerabilityType.URGENCY)
        );
    }
}
