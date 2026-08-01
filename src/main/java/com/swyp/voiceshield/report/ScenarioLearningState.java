package com.swyp.voiceshield.report;

/**
 * 추천 목록에 담기는 사례의 학습 상태.
 *
 * <p>완료(COMPLETE)한 사례는 추천 대상이 아니므로 여기에 없다.
 */
public enum ScenarioLearningState {

    /** 학습 이력이 없다. */
    NOT_STARTED,

    /** 시작했으나 끝내지 않았다. 같은 조건이면 이쪽을 먼저 추천한다. */
    IN_PROGRESS
}
