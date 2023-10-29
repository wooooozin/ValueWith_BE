package com.valuewith.tweaver.auth.dto;

import com.valuewith.tweaver.user.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public class AuthDto {

  // TODO:인증, 로그인

  @Data
  @NoArgsConstructor
  public static class SignUpForm {

    private String nickname;
    private String email;
    private String password;
    private String gender;
    private Integer age;
    private String profileUrl;

    public Member toEntity() {
      return Member.builder()
          .nickName(this.nickname)
          .email(this.email)
          .password(this.password)
          .gender(this.gender)
          .age(this.age)
          .profileUrl(this.profileUrl)
          .isSocial(Boolean.FALSE)
          .build();
    }
  }

}
