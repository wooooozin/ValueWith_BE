package com.valuewith.tweaver.groupMember.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupMemberService {

  private final GroupMemberRepository groupMemberRepository;

  public void createGroupMember(TripGroup tripGroup, Member member, ChatRoom chatRoom) {
    GroupMember groupMember = GroupMember.builder()
        .tripGroup(tripGroup)
        .member(member)
        .chatRoom(chatRoom)
        .isBanned(false)
        .approvedStatus(ApprovedStatus.APPROVED)
        .approvedDateTime(LocalDateTime.now())
        .build();

    groupMemberRepository.save(groupMember);
  }

  public void deleteGroupMember(Long tripGroupId) {
    groupMemberRepository.deleteGroupMemberByTripGroupTripGroupId(tripGroupId);
  }

  public List<GroupMember> findApprovedGroupByMemberId(Long memberId) {
    return groupMemberRepository.findGroupMembersByMember_MemberIdAndApprovedStatus(
        memberId, ApprovedStatus.APPROVED);
  }
}
