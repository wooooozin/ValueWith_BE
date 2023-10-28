package com.valuewith.tweaver.member.repository;

import com.valuewith.tweaver.member.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<GroupMember, Long> {

}
