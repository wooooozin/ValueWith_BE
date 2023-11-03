package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.group.dto.TripGroupListResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupResponseDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripGroupListService {

    private final TripGroupRepository tripGroupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public TripGroupListResponseDto getFilteredTripGroups(
        String status, String area, String title, Pageable pageable
    ) {
        Page<TripGroup> page = tripGroupRepository.findByStatusAndTripAreaAndNameContainingIgnoreCase(
            GroupStatus.valueOf(status.toUpperCase()), area, title, pageable
        );
        List<TripGroupResponseDto> tripGroupResponseDtoList = page.getContent().stream()
            .map(tripGroup -> {
                int currentMembersCount = groupMemberRepository.countApprovedMembers(tripGroup, ApprovedStatus.APPROVED) + 1;
                return TripGroupResponseDto.from(tripGroup, currentMembersCount);

            })
            .collect(Collectors.toList());

        return TripGroupListResponseDto.from(
            tripGroupResponseDtoList,
            page.getNumber(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.isLast()
        );
    }
}