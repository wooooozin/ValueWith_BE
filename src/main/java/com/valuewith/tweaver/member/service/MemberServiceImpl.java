package com.valuewith.tweaver.member.service;

import com.valuewith.tweaver.chat.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.MemberRole;
import com.valuewith.tweaver.group.Group;
import com.valuewith.tweaver.member.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import com.valuewith.tweaver.user.User;
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
  public void setMember(Group group, User user, ChatRoom chatRoom) {
    Member member = Member.builder()
        .memberRole(MemberRole.LEADER)
        .group(group)
        .user(user)
        .chatRoom(chatRoom)
        .memberRole(MemberRole.LEADER)
        .isBanned(false)
        .approvedStatus(ApprovedStatus.APPROVED)
        .build();

      memberRepository.save(member);
  }
}
