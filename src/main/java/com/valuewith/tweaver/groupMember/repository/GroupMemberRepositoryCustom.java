package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.groupMember.entity.GroupMember;
import java.util.List;

public interface GroupMemberRepositoryCustom {
    List<GroupMember> findApprovedMembersByTripGroupId(Long tripGroupId);

}
