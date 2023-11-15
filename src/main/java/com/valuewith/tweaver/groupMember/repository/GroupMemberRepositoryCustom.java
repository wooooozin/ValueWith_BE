package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import java.util.List;

public interface GroupMemberRepositoryCustom {
    List<GroupMember> findApprovedMembersByTripGroupId(Long tripGroupId);

    List<GroupMember> findGroupMembersByTripGroupAndApprovedStatus(Long tripGroupId, ApprovedStatus approvedStatus);

    List<GroupMember> findApprovedMembersByTripGroupIdAndMemberId(Long tripGroupId, Long groupLeaderId, Long memberId);

    List<GroupMember> findApprovedMembersByTripGroupIdExceptLeader(Long groupLeaderId, Long tripGroupId);

    GroupMember findApprovedMemberByTripGroupIdAndMemberId(Long tripGroupId, Long memberId);
}
