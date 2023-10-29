package com.valuewith.tweaver.groupMember.repository;

import com.valuewith.tweaver.groupMember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<GroupMember, Long> {

}
