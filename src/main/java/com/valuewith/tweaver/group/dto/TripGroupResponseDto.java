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
    private String leaderEmail;
    private String profileUrl;
    private String nickName;
    private String age;
    private String gender;

    public static TripGroupResponseDto from(TripGroup tripGroup) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return TripGroupResponseDto.builder()
            .tripGroupId(tripGroup.getTripGroupId())
            .name(tripGroup.getName())
            .content(tripGroup.getContent())
            .maxUserNumber(tripGroup.getMaxMemberNumber())
            .currentUserNumber(tripGroup.getCurrentMemberNumber() + 1)
            .tripArea(tripGroup.getTripArea())
            .tripDate(tripGroup.getTripDate().format(formatter))
            .dueDate(tripGroup.getDueDate().format(formatter))
            .createdAt(tripGroup.getCreatedDateTime().format(formatter))
            .status(tripGroup.getStatus().getDescription())
            .thumbnailUrl(tripGroup.getThumbnailUrl())
            .profileUrl(tripGroup.getMember().getProfileUrl())
            .leaderEmail(tripGroup.getMember().getEmail())
            .nickName(tripGroup.getMember().getNickName())
            .age(tripGroup.getMember().getAge().toString())
            .gender(tripGroup.getMember().getGender())
            .build();
    }


}
