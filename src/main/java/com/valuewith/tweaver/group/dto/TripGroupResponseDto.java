package com.valuewith.tweaver.group.dto;

import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.member.entity.Member;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TripGroupResponseDto {

    private Long tripGroupId;
    private String name;
    private String content;
    private Integer maxUserNumber;
    private Integer currentUserNumber;
    private String tripArea;
    private String tripDate;
    private String dueDate;
    private String createdAt;
    private String status;
    private String thumbnailUrl;
    private String profileUrl;
    private String nickName;
    private String age;
    private String gender;

    public static TripGroupResponseDto from(
        TripGroup tripGroup, Integer currentUserNumber
    ) {
        Member member = tripGroup.getMember();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return TripGroupResponseDto.builder()
            .tripGroupId(tripGroup.getTripGroupId())
            .name(tripGroup.getName())
            .content(tripGroup.getContent())
            .maxUserNumber(tripGroup.getMaxMemberNumber())
            .currentUserNumber(currentUserNumber)
            .tripArea(tripGroup.getTripArea())
            .tripDate(tripGroup.getTripDate().format(formatter))
            .dueDate(tripGroup.getDueDate().format(formatter))
            .createdAt(tripGroup.getCreatedDateTime().format(formatter))
            .thumbnailUrl(tripGroup.getThumbnailUrl())
            .status(tripGroup.getStatus().getDescription())
            .profileUrl(member.getProfileUrl())
            .nickName(member.getNickName())
            .age(member.getAge().toString())
            .gender(member.getGender())
            .build();
    }

}
