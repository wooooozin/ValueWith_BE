package com.valuewith.tweaver.groupMember.dto;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.message.entity.Message;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupMemberDetailResponseDto {

    private String groupMemberNickname;
    private String groupMemberAge;
    private String groupMemberGender;
    private String groupMemberProfileUrl;
    public static GroupMemberDetailResponseDto from(
        GroupMember groupMember
    ) {
        return GroupMemberDetailResponseDto.builder()
            .groupMemberNickname(groupMember.getMember().getNickName())
            .groupMemberAge(groupMember.getMember().getAge().toString())
            .groupMemberGender(groupMember.getMember().getGender())
            .groupMemberProfileUrl(groupMember.getMember().getProfileUrl())
            .build();
    }

}
