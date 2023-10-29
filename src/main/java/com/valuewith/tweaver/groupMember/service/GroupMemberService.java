package com.valuewith.tweaver.groupMember.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.MemberRole;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.MemberRepository;
import com.valuewith.tweaver.menber.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupMemberService {
  private final MemberRepository memberRepository;

  public void createGroupMember(TripGroup tripGroup, Member member, ChatRoom chatRoom) {
    GroupMember groupMember = GroupMember.builder()
        .memberRole(MemberRole.LEADER)
        .tripGroup(tripGroup)
        .member(member)
        .chatRoom(chatRoom)
        .memberRole(MemberRole.LEADER)
        .isBanned(false)
        .approvedStatus(ApprovedStatus.APPROVED)
        .build();

      memberRepository.save(groupMember);
  }
}
