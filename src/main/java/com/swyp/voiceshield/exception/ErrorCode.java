package com.swyp.voiceshield.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 요청입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-002", "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-003", "서버 내부 오류가 발생했습니다."),

    CASE_SCENARIO_NOT_FOUND(HttpStatus.NOT_FOUND, "CASE-001", "해당 사례 시나리오를 찾을 수 없습니다."),
    CASE_VARIANT_NOT_FOUND(HttpStatus.NOT_FOUND, "CASE-002", "해당 사례 버전을 찾을 수 없습니다."),
    CASE_CHOICE_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "CASE-003", "해당 선택지를 찾을 수 없습니다."),
    CASE_CHANNEL_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "CASE-004", "지원하지 않는 사례 채널입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
