package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>,
    GroupMemberRepositoryCustom {

  void deleteGroupMemberByTripGroupTripGroupId(Long tripGroupId);

  List<GroupMember> findGroupMembersByMember_MemberIdAndApprovedStatus(
      Long memberId, ApprovedStatus approvedStatus);

  Boolean existsByMember_MemberIdAndTripGroup_TripGroupId(
      Long memberId, Long tripGroupId);

  GroupMember findGroupMemberByMember_MemberIdAndTripGroup_TripGroupId(
      Long memberId, Long tripGroupId);

  Optional<GroupMember> findByGroupMemberId(Long groupMemberId);
}
