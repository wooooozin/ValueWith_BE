package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.groupMember.dto.GroupMemberListDto;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import java.util.List;

public interface GroupMemberRepositoryCustom {
    List<GroupMember> findApprovedMembersByTripGroupId(Long tripGroupId);

    List<GroupMember> findGroupMembersByTripGroupAndApprovedStatus(Long tripGroupId, ApprovedStatus approvedStatus);
}
