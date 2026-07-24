package com.swyp.voiceshield.auth;

record KakaoLoginResult(
        KakaoLoginResponse response,
        boolean created
) {
}
