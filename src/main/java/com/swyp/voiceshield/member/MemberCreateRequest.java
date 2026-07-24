package com.swyp.voiceshield.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberCreateRequest(
        @NotBlank String userId,
        @Pattern(regexp = "SIGNUP_COMPLETE")
        @NotBlank String signupStatus,
        String name,
        String nickname
) {
}
