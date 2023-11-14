package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>,
    GroupMemberRepositoryCustom {

  void deleteGroupMemberByTripGroupTripGroupId(Long tripGroupId);

  List<GroupMember> findGroupMembersByMember_MemberIdAndApprovedStatus(Long memberId,
      ApprovedStatus approvedStatus);
}
