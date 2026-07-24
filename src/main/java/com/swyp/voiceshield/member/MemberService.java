package com.swyp.voiceshield.member;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import com.swyp.voiceshield.user.AppUser;
import com.swyp.voiceshield.user.AppUserRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private static final String SIGNUP_COMPLETE = "SIGNUP_COMPLETE";

    private final AppUserRepository appUserRepository;
    private final MemberProfileRepository memberProfileRepository;

    public MemberService(AppUserRepository appUserRepository, MemberProfileRepository memberProfileRepository) {
        this.appUserRepository = appUserRepository;
        this.memberProfileRepository = memberProfileRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        AppUser user = appUserRepository.findById(request.userId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.completeSignup();
        MemberProfile memberProfile = memberProfileRepository.save(
                MemberProfile.create(user, SIGNUP_COMPLETE, LocalDateTime.now())
        );
        return new MemberResponse(memberProfile.getMemberId(), memberProfile.getSignupStatus());
    }

    @Transactional(readOnly = true)
    public MemberMeResponse getMyMemberProfile(String userId) {
        MemberProfile memberProfile = memberProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_PROFILE_NOT_FOUND));
        return new MemberMeResponse(
                memberProfile.getMemberId(),
                memberProfile.getUser().getUserId(),
                memberProfile.getSignupStatus()
        );
    }

    @Transactional
    public void withdrawMember(String userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        memberProfileRepository.deleteByUser_UserId(userId);
        appUserRepository.delete(user);
    }
}
