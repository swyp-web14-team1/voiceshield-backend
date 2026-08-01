package com.swyp.voiceshield.report;

/**
 * 리포트를 보여줄 수 있는 상태인지.
 *
 * <p>프론트는 이 값만 보고 화면을 가른다. {@code weakTypes}의 길이로 판단하지 않도록
 * 명시적으로 내려준다.
 */
public enum ReportStatus {

    /** 표시할 취약 유형이 있다. */
    READY,

    /** 어떤 유형도 최소 시도 수를 넘기지 못했다. 안내 문구만 보여준다. */
    NOT_ENOUGH_DATA
}
