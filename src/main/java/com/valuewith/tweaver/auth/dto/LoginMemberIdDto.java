package com.valuewith.tweaver.auth.dto;

import com.valuewith.tweaver.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginMemberIdDto {
  private Long memberId;
  private String memberNickname;
  private String memberEmail;
  private String memberProfileUrl;

  public static LoginMemberIdDto from(Member member) {
    return LoginMemberIdDto.builder()
        .memberId(member.getMemberId())
        .memberNickname(member.getNickName())
        .memberEmail(member.getEmail())
        .memberProfileUrl(member.getProfileUrl())
        .build();
  }
}
