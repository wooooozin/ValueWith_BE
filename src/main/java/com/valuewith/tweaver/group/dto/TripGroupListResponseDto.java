package com.valuewith.tweaver.group.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TripGroupListResponseDto {
    private Integer currentPage;
    private Integer totalPages;
    private Long totalElements;
    private boolean last;
    private List<TripGroupResponseDto> tripGroups;

    public static TripGroupListResponseDto from(
        List<TripGroupResponseDto> tripGroupResponseDtos,
        Integer pageNumber,
        Integer totalPages,
        Long totalElements,
        boolean last
    ) {
        return TripGroupListResponseDto.builder()
            .tripGroups(tripGroupResponseDtos)
            .currentPage(pageNumber)
            .totalPages(totalPages)
            .totalElements(totalElements)
            .last(last)
            .build();
    }
}

