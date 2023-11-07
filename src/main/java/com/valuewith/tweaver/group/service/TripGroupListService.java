package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.group.dto.TripGroupDetailResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupListResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupStatusResponseDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.dto.GroupMemberDetailResponseDto;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

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

    public List<TripGroupStatusResponseDto> getMyTripGroupList(
        String memberEmail, String status
    ) {
        Member member = memberRepository.findByEmail(memberEmail)
            .orElseThrow(() -> new EntityNotFoundException("이메일 가입정보가 없습니다.. email: " + memberEmail));
        List<TripGroup> tripGroups = null;
        switch (status) {
            case "leader":
                tripGroups = tripGroupRepository.findLeaderTripGroups(member.getMemberId());
                break;
            case "approved":
                tripGroups = tripGroupRepository.findApprovedGroups(member.getMemberId());
                break;
            case "pending":
                tripGroups = tripGroupRepository.findPendingGroups(member.getMemberId());
                break;
            default:
                throw new IllegalArgumentException("올바르지 않은 접근입니다.");
        }
        return tripGroups.stream()
            .map(TripGroupStatusResponseDto::from)
            .collect(Collectors.toList());
    }
}

