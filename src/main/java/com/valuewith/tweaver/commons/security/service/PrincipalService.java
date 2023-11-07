package com.valuewith.tweaver.commons.security.service;

import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = this.memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("이메일을 찾을 수 없습니다. -> " + email));

    return new PrincipalDetails(member);
  }
}
