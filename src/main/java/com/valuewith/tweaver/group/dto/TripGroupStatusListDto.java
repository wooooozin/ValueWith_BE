package com.valuewith.tweaver.group.dto;

import com.valuewith.tweaver.member.entity.Member;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class TripGroupStatusListDto {
    private List<TripGroupStatusResponseDto> tripGroups;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public static TripGroupStatusListDto from (
        Page<TripGroupStatusResponseDto> tripGroupPage
    ) {
        return TripGroupStatusListDto.builder()
            .tripGroups(tripGroupPage.getContent())
            .currentPage(tripGroupPage.getNumber() + 1)
            .totalPages(tripGroupPage.getTotalPages())
            .totalElements(tripGroupPage.getTotalElements())
            .build();
    }
}
