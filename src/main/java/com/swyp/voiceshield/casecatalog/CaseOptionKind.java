package com.swyp.voiceshield.casecatalog;

/**
 * 선택지의 종류.
 *
 * <p>논리 모델(04. 모델링 C54)은 선택지를 "행동 또는 답안 후보"로 규정한다.
 * 한 테이블이 두 쓰임을 겸하므로 구분자가 필요하다.
 */
public enum CaseOptionKind {

    /** 정본 '🧩 단서 찾기 퀴즈'의 보기. 퀴즈 채점에 쓰인다. */
    QUIZ,

    /** 정본 '🎮 선택지'. 시뮬레이션 진행 중 사용자가 고르는 행동. */
    ACTION
}
