package com.swyp.voiceshield.report;

/**
 * 유형별 채점 가능 여부.
 *
 * <p>"약하다"와 "아직 모른다"를 반드시 구분하기 위해 존재한다. 시도가 적은 유형을 0%로
 * 표시하면, 안 해본 것이 가장 약한 유형으로 올라온다.
 */
public enum ScoreStatus {

    /** 최소 시도 수를 넘겨 점수를 낼 수 있는 상태. */
    SCORED,

    /** 시도가 부족해 판단할 수 없는 상태. <b>약한 것이 아니라 모르는 것이다.</b> */
    UNKNOWN
}
