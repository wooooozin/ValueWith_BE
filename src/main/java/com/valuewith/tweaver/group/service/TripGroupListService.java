package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.group.dto.TripGroupDetailResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupListResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupResponseDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.dto.GroupMemberDetailResponseDto;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.place.dto.PlaceDetailResponseDto;
import com.valuewith.tweaver.place.repository.PlaceRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripGroupListService {

    private final TripGroupRepository tripGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PlaceRepository placeRepository;

    public TripGroupListResponseDto getFilteredTripGroups(
        String status, String area, String title, Pageable pageable
    ) {

        List<TripGroup> tripGroups = tripGroupRepository.findFilteredTripGroups(status, area, title, pageable);
        List<TripGroupResponseDto> tripGroupResponseDtoList = tripGroups.stream()
            .map(TripGroupResponseDto::from)
            .collect(Collectors.toList());

        long total = tripGroupRepository.countFilteredTripGroups(status, area, title);
        int totalPages = (int) Math.ceil((double) total / pageable.getPageSize());
        boolean isLast = pageable.getOffset() + pageable.getPageSize() >= total;

        return TripGroupListResponseDto.from(
            tripGroupResponseDtoList,
            pageable.getPageNumber(),
            totalPages,
            total,
            isLast
        );
    }

    public TripGroupDetailResponseDto getTripGroupDetail(Long tripGroupId) {
        TripGroup tripGroup = tripGroupRepository.findByTripGroupId(tripGroupId)
            .orElseThrow(() -> new EntityNotFoundException("등록된 여행 정보가 없습니다. id: " + tripGroupId));

        TripGroupResponseDto tripGroupResponseDto = TripGroupResponseDto.from(tripGroup);

        List<GroupMemberDetailResponseDto> groupMembers = groupMemberRepository
            .findApprovedMembersByTripGroupId(tripGroup.getTripGroupId())
            .stream()
            .map(GroupMemberDetailResponseDto::from)
            .collect(Collectors.toList());

        List<PlaceDetailResponseDto> places = placeRepository.findByTripGroup(tripGroup)
            .stream()
            .map(PlaceDetailResponseDto::from)
            .collect(Collectors.toList());

        return TripGroupDetailResponseDto.from(
            tripGroupResponseDto, groupMembers, places
        );
    }

}

