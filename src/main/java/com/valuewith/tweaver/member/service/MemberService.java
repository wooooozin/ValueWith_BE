package com.valuewith.tweaver.member.service;

import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;

  public Member findMemberByEmail(String memberEmail) {
    return memberRepository.findByEmail(memberEmail)
        .orElseThrow(() -> new RuntimeException("멤버가 존재하지 않습니다."));
  }

}
