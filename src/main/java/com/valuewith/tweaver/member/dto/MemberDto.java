package com.valuewith.tweaver.member.dto;

import com.valuewith.tweaver.alert.dto.AlertDto;
import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.groupMember.dto.GroupMemberDto;
import com.valuewith.tweaver.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberDto {
  private Long memberId;
  private String email;
  private String password;
  private String nickName;
  private Integer age;
  private String gender;
  private String profileUrl;
  private Provider provider;
  private String providerId;
  private String refreshToken;
  private List<GroupMemberDto> groupMembers = new ArrayList<>();
  private List<AlertDto> alerts = new ArrayList<>();

  public MemberDto from(Member member) {
    return MemberDto.builder()
        .email(member.getEmail())
        .password(member.getPassword())
        .nickName(member.getNickName())
        .age(member.getAge())
        .gender(member.getGender())
        .profileUrl(member.getProfileUrl())
        .provider(member.getProvider())
        .providerId(member.getProviderId())
        .refreshToken(member.getRefreshToken())
        .build();
  }
}
