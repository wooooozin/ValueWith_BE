package com.valuewith.tweaver.auth.dto;

import com.valuewith.tweaver.user.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

  // TODO: 로그인

  @Data
  @NoArgsConstructor
  public static class SignUpForm {

    private String nickname;
    private String email;
    private String password;
    private String gender;
    private Integer age;

    public Member toEntity(String profileUrl) {
      return Member.builder()
          .nickName(this.nickname)
          .email(this.email)
          .password(this.password)
          .gender(this.gender)
          .age(this.age)
          .profileUrl(profileUrl)
          .isSocial(Boolean.FALSE)
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  public static class VerificationForm {
    private String email;
    private String code;
  }

  @Data
  @NoArgsConstructor
  public static class EmailInput {
    private String email;
  }
}
