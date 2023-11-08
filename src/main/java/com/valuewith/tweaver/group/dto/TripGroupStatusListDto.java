package com.valuewith.tweaver.group.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TripGroupStatusListDto {
    private List<TripGroupStatusResponseDto> tripGroups;

    public static TripGroupStatusListDto from (
        List<TripGroupStatusResponseDto> tripGroupStatusResponseDto
    ) {
        return TripGroupStatusListDto.builder()
            .tripGroups(tripGroupStatusResponseDto)
            .build();
    }
}
