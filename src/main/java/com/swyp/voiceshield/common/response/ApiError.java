package com.swyp.voiceshield.common.response;

public record ApiError(
        String code,
        String message
) {
}
