package com.swyp.voiceshield.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByProviderAndProviderUserId(String provider, String providerUserId);
}
