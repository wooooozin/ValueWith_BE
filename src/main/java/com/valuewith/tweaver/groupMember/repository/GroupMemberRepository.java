package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.groupMember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
  void deleteGroupMemberByTripGroupTripGroupId(Long tripGroupId);
}
