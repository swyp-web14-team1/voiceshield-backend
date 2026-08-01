package com.swyp.voiceshield.report;

import java.util.Map;
import java.util.Set;

/**
 * 시나리오가 어떤 취약 유형을 다루는지에 대한 매핑. <b>A(취약 유형 분석 엔진)와의 두 번째 접점.</b>
 *
 * <p>선택지 단위 태깅이 A의 산출물이고, 이걸 시나리오 단위로 모은 것이 여기서 필요한 값이다.
 * A의 태깅과 어긋나면 엉뚱한 사례를 추천하게 되므로 최종적으로는 A 쪽 값을 써야 한다.
 */
public interface ScenarioTypeMappingProvider {

    /**
     * 시나리오 ID → 그 시나리오에서 다루는 취약 유형 집합.
     *
     * <p>매핑이 없는 시나리오는 키를 넣지 않아도 된다. 추천에서 제외되지는 않고,
     * "약한 유형과 연결되지 않은 사례"로 후순위가 된다.
     */
    Map<String, Set<VulnerabilityType>> getMapping();
}
