package com.swyp.voiceshield.member;

import com.swyp.voiceshield.common.response.ApiResponse;
import com.swyp.voiceshield.common.identity.UserIdentifier;
import com.swyp.voiceshield.common.identity.UserIdentifierResolver;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final UserIdentifierResolver userIdentifierResolver;

    public MemberController(MemberService memberService, UserIdentifierResolver userIdentifierResolver) {
        this.memberService = memberService;
        this.userIdentifierResolver = userIdentifierResolver;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @Valid @RequestBody MemberCreateRequest request
    ) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ApiResponse<MemberMeResponse> getMyMemberProfile(
            @RequestHeader(value = UserIdentifierResolver.USER_ID_HEADER, required = false) String userId
    ) {
        UserIdentifier userIdentifier = userIdentifierResolver.requireUser(userId);
        return ApiResponse.success(memberService.getMyMemberProfile(userIdentifier.userId()));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdrawMyMembership(
            @RequestHeader(value = UserIdentifierResolver.USER_ID_HEADER, required = false) String userId
    ) {
        UserIdentifier userIdentifier = userIdentifierResolver.requireUser(userId);
        memberService.withdrawMember(userIdentifier.userId());
        return ResponseEntity.noContent().build();
    }
}
