package com.valuewith.tweaver.member.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.UserRole;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.member.entity.GroupUser;
import com.valuewith.tweaver.member.repository.MemberRepository;
import com.valuewith.tweaver.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberServiceImpl implements MemberService{
  private final MemberRepository memberRepository;

  @Override
  public void setMember(TripGroup tripGroup, User user, ChatRoom chatRoom) {
    GroupUser groupUser = GroupUser.builder()
        .userRole(UserRole.LEADER)
        .tripGroup(tripGroup)
        .user(user)
        .chatRoom(chatRoom)
        .userRole(UserRole.LEADER)
        .isBanned(false)
        .approvedStatus(ApprovedStatus.APPROVED)
        .build();

      memberRepository.save(groupUser);
  }
}
