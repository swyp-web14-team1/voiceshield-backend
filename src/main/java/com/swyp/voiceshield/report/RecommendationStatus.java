package com.swyp.voiceshield.report;

/**
 * 추천할 사례가 있는지.
 *
 * <p>프론트는 이 값으로 화면을 가른다. 목록 길이로 판단하지 않도록 명시적으로 내려준다.
 */
public enum RecommendationStatus {

    /** 추천할 사례가 있다. */
    READY,

    /** 노출 중인 사례를 모두 완료해 추천할 것이 없다. */
    ALL_COMPLETED
}
