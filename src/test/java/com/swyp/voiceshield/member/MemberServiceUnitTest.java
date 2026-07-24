package com.swyp.voiceshield.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.swyp.voiceshield.user.AppUser;
import com.swyp.voiceshield.user.AppUserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceUnitTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void withdrawMemberMarksUserAsDeleted() {
        AppUser user = AppUser.createKakao("kakao-member-withdraw", LocalDateTime.now());
        user.completeSignup();

        when(appUserRepository.findByIdAndDeletedAtIsNull(user.getUserId()))
                .thenReturn(Optional.of(user));

        memberService.withdrawMember(user.getUserId());

        assertThat(user.isDeleted()).isTrue();
        verifyNoInteractions(memberProfileRepository);
        verify(appUserRepository).findByIdAndDeletedAtIsNull(user.getUserId());
    }
}
