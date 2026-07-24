package com.swyp.voiceshield.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, String> {

    Optional<MemberProfile> findByUser_UserIdAndUser_DeletedAtIsNull(String userId);
}
