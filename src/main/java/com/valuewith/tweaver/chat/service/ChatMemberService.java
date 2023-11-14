package com.valuewith.tweaver.chat.service;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

  private final GroupMemberRepository groupMemberRepository;

  public List<GroupMember> findMyGroupsByMemberId(Long groupMemberId) {
    return groupMemberRepository.findGroupMembersByMember_MemberIdAndApprovedStatus(groupMemberId,
        ApprovedStatus.APPROVED);
  }
}
