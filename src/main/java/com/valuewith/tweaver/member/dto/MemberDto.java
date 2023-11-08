package com.valuewith.tweaver.member.dto;

import com.valuewith.tweaver.alert.dto.AlertResponseDto;
import com.valuewith.tweaver.groupMember.dto.GroupMemberDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {
  private Long memberId;
  private String email;
  private String password;
  private String nickName;
  private Integer age;
  private String gender;
  private String profileUrl;
  private Boolean isSocial;
  private List<GroupMemberDto> groupMembers = new ArrayList<>();
  private List<AlertResponseDto> alerts = new ArrayList<>();
}
