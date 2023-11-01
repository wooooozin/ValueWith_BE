package com.valuewith.tweaver.member.repository;

import com.valuewith.tweaver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Boolean existsByEmail(String email);

}
