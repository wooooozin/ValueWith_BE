package com.valuewith.tweaver.commons;

import com.valuewith.tweaver.member.entity.Member;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@AllArgsConstructor
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

  private Member member;
  private Map<String, Object> attributes;
  private Collection<GrantedAuthority> authorities;

  // 일반 로그인 생성자
  public PrincipalDetails(Member member) {
    this.member = member;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return this.attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  @Override
  public String getUsername() {
    return member.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getName() {
    return null;
  }
}
