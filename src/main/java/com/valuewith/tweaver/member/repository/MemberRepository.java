package com.valuewith.tweaver.member.repository;

import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Boolean existsByEmail(String email);

  Optional<Member> findByEmail(String email);

  Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);

  Optional<Member> findByRefreshToken(String refreshToken);
}
