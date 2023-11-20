package com.valuewith.tweaver.groupMember.dto;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupMemberDetailResponseDto {

    private Long memberId;
    private String groupMemberEmail;
    private String groupMemberNickname;
    private Integer groupMemberAge;
    private String groupMemberGender;
    private String groupMemberProfileUrl;
    private String groupMemberStatus;
    public static GroupMemberDetailResponseDto from(
        GroupMember groupMember
    ) {
        return GroupMemberDetailResponseDto.builder()
            .memberId(groupMember.getMember().getMemberId())
            .groupMemberEmail(groupMember.getMember().getEmail())
            .groupMemberNickname(groupMember.getMember().getNickName())
            .groupMemberAge(groupMember.getMember().getAge())
            .groupMemberGender(groupMember.getMember().getGender())
            .groupMemberProfileUrl(groupMember.getMember().getProfileUrl())
            .groupMemberStatus(groupMember.getApprovedStatus().name().toLowerCase())
            .build();
    }

}
