package com.valuewith.tweaver.groupMember.service;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.dto.GroupMemberListDto;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupMemberListService {

    private final MemberRepository memberRepository;
    private final TripGroupRepository tripGroupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public List<GroupMemberListDto> getFilteredGroupMembers(
            String memberEmail, Long tripGroupId, String status
    ) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("이메일 가입정보가 없습니다. email: " + memberEmail));

        TripGroup tripGroup = tripGroupRepository.findByTripGroupId(tripGroupId)
                .orElseThrow(() -> new EntityNotFoundException("여행 정보가 없습니다. ID: " + tripGroupId));

        if (!member.getMemberId().equals(tripGroup.getMember().getMemberId())) {
            throw new IllegalArgumentException("여행 그룹 멤버 보기 권한이 없습니다.");
        }
        ApprovedStatus approvedStatus = setApprovedStatus(status);
        List<GroupMember> groupMembers = groupMemberRepository.findGroupMembersByTripGroupAndApprovedStatus(
                tripGroupId, approvedStatus
        );

        return groupMembers.stream()
                .map(GroupMemberListDto::from)
                .collect(Collectors.toList());
    }

    private ApprovedStatus setApprovedStatus(String status) {
        if ("approved".equals(status)) {
            return ApprovedStatus.APPROVED;
        } else if ("pending".equals(status)) {
            return ApprovedStatus.PENDING;
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    //TODO - 채팅도 이때 함께 나가지는게 좋을지 고민
    @Transactional
    public void leftMemberFromTripGroup(String memberEmail, Long tripGroupId) {
        Member member = memberRepository.findByEmail(memberEmail)
            .orElseThrow(() -> new EntityNotFoundException("이메일 가입정보가 없습니다. email: " + memberEmail));

        TripGroup tripGroup = tripGroupRepository.findByTripGroupId(tripGroupId)
            .orElseThrow(() -> new EntityNotFoundException("여행 정보가 없습니다. ID: " + tripGroupId));

        GroupMember groupMember = groupMemberRepository.findApprovedMemberByTripGroupIdAndMemberId(
            tripGroup.getTripGroupId(), member.getMemberId()
        );

        // 채팅방 아웃 예시
        // chatRoomService.removeMemberFromChatRoom(member, groupMember.getChatRoom());
        groupMember.leaveApplication(ApprovedStatus.LEFT);
        groupMemberRepository.save(groupMember);

        // 현재 멤버 수 감소
        tripGroup.decrementCurrentMemberNumber();
        tripGroup.setGroupStatus();
        tripGroupRepository.save(tripGroup);
    }


    @Transactional
    public Member bannedGroupMemberFromTripGroup(
        String memberEmail, Long tripGroupId, Long groupMemberId
    ) {
        Member member = memberRepository.findByEmail(memberEmail)
            .orElseThrow(() -> new EntityNotFoundException("이메일 가입정보가 없습니다. email: " + memberEmail));

        TripGroup tripGroup = tripGroupRepository.findByTripGroupId(tripGroupId)
            .orElseThrow(() -> new EntityNotFoundException("여행 정보가 없습니다. ID: " + tripGroupId));

        if (!member.getMemberId().equals(tripGroup.getMember().getMemberId())) {
            throw new IllegalArgumentException("그룹멤버 추방 권한이 없습니다.");
        }
        GroupMember groupMember = groupMemberRepository.findByGroupMemberId(groupMemberId)
            .orElseThrow(() -> new EntityNotFoundException("등록된 그룹멤버 정보가 없습니다." + groupMemberId));
        groupMember.leaveApplication(ApprovedStatus.BANNED);
        groupMemberRepository.save(groupMember);

        // 현재 멤버 수 감소
        tripGroup.decrementCurrentMemberNumber();
        tripGroup.setGroupStatus();
        tripGroupRepository.save(tripGroup);

        return groupMember.getMember();
    }
}
