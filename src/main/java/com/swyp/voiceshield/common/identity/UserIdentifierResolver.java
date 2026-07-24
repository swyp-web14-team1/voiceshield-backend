package com.swyp.voiceshield.common.identity;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class UserIdentifierResolver {

    public static final String USER_ID_HEADER = "X-User-Id";

    public UserIdentifier requireUser(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ApiException(ErrorCode.AUTH_REQUIRED);
        }
        return new UserIdentifier(userId);
    }
}
