package com.valuewith.tweaver.auth.dto;

import com.valuewith.tweaver.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    // TODO: to 메소드 사용법 변경
    public Member setProfileUrl(String profileUrl) {
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
