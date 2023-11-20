package com.valuewith.tweaver.member.service;

import static com.valuewith.tweaver.constants.ErrorCode.*;

import com.valuewith.tweaver.constants.ErrorCode;
import com.valuewith.tweaver.exception.CustomException;
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
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
  }

  public Member findMemberByMemberId(Long id) {
    return memberRepository.findById(id).orElseThrow(
        () -> new CustomException(MEMBER_NOT_FOUND)
    );
  }
}
