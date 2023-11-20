package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import java.util.List;
import java.util.Optional;

public interface GroupMemberRepositoryCustom {
    List<GroupMember> findApprovedMembersByTripGroupId(Long tripGroupId);

    List<GroupMember> findApprovedAndPendingMembersByTripGroupId(Long tripGroupId);

    List<GroupMember> findGroupMembersByTripGroupAndApprovedStatus(Long tripGroupId, ApprovedStatus approvedStatus);

    List<GroupMember> findApprovedMembersByTripGroupIdAndNotInMemberId(Long tripGroupId, Long memberId);

    GroupMember findApprovedMemberByTripGroupIdAndMemberId(Long tripGroupId, Long memberId);

    Optional<GroupMember> findPendingMembersByTripGroupIdAndMemberId(Long tripGroupId, Long memberId);
}
