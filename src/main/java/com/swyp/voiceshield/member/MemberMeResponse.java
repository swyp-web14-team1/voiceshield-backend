package com.swyp.voiceshield.member;

public record MemberMeResponse(
        String memberId,
        String userId,
        String signupStatus,
        String name,
        String nickname
) {
}
