package com.valuewith.tweaver.auth.dto;

import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.member.entity.Member;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class SignInForm {

    @Email
    private String email;
    private String password;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Setter
  public static class SignUpForm {

    private String nickname;
    @Email
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
          .provider(Provider.NORMAL)
          .profileUrl(profileUrl)
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class VerificationForm {

    @Email
    private String email;
    private String code;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class EmailInput {

    @Email
    private String email;
  }
}
