package com.valuewith.tweaver.group.dto;

import com.valuewith.tweaver.groupMember.dto.GroupMemberDetailResponseDto;
import com.valuewith.tweaver.place.dto.PlaceDetailResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TripGroupDetailResponseDto {

    private TripGroupResponseDto tripGroupDetail;
    private List<GroupMemberDetailResponseDto> groupMembers;
    private List<PlaceDetailResponseDto> places;

    public static TripGroupDetailResponseDto from(
        TripGroupResponseDto tripGroupResponseDto,
        List<GroupMemberDetailResponseDto> groupMemberDetailResponseDto,
        List<PlaceDetailResponseDto> placeDetailResponseDto
    ) {
        return TripGroupDetailResponseDto.builder()
            .tripGroupDetail(tripGroupResponseDto)
            .groupMembers(groupMemberDetailResponseDto)
            .places(placeDetailResponseDto)
            .build();
    }
}
