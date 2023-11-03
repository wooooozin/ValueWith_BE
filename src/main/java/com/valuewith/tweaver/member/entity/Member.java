package com.valuewith.tweaver.member.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import java.util.Collection;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "MEMBER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE MEMBER SET IS_DELETED = 1 WHERE MEMBER_ID = ?")
public class Member extends BaseEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @NotNull
  @Column(unique = true)
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String nickName;

  @NotNull
  private Integer age;

  @NotNull
  private String gender;

  @NotNull
  private String profileUrl;

  @NotNull
  private Boolean isSocial;

  /**
   * 스프링 시큐리티에서 제공하는 기본 디테일입니다.
   * 본 멤버에서는 로그인이후
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getUsername() {
    return this.email;
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
}
