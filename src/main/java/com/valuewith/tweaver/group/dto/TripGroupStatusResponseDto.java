package com.valuewith.tweaver.group.dto;

import com.valuewith.tweaver.group.entity.TripGroup;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TripGroupStatusResponseDto {

    private Long tripGroupId;
    private String name;
    private String content;
    private Integer maxUserNumber;
    private Integer currentUserNumber;
    private String tripArea;
    private String tripDate;
    private String dueDate;
    private String thumbnailUrl;
    private String status;
    private String leaderEmail;
    private String profileUrl;
    private String nickName;
    private Integer age;
    private String gender;

    public static TripGroupStatusResponseDto from(TripGroup tripGroup) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return TripGroupStatusResponseDto.builder()
            .tripGroupId(tripGroup.getTripGroupId())
            .name(tripGroup.getName())
            .content(tripGroup.getContent())
            .maxUserNumber(tripGroup.getMaxMemberNumber())
            .currentUserNumber(tripGroup.getCurrentMemberNumber())
            .tripArea(tripGroup.getTripArea())
            .tripDate(tripGroup.getTripDate().format(formatter))
            .dueDate(tripGroup.getDueDate().format(formatter))
            .thumbnailUrl(tripGroup.getThumbnailUrl())
            .status(tripGroup.getStatus().getDescription())
            .leaderEmail(tripGroup.getMember().getEmail())
            .profileUrl(tripGroup.getMember().getProfileUrl())
            .nickName(tripGroup.getMember().getNickName())
            .age(tripGroup.getMember().getAge())
            .gender(tripGroup.getMember().getGender())
            .build();
    }


}
