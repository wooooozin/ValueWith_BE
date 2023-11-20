package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

    Provider memberProvider = member.getProvider();
    if (memberProvider != Provider.NORMAL) {
      //TODO: 소셜로그인 Provider 안맞을 경우 생기는 익셉션 추가
      throw new RuntimeException(memberProvider + " 소셜 로그인 계정입니다.");
    }
    return new PrincipalDetails(member);
  }
}
