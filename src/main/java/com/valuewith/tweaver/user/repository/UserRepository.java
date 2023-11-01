package com.valuewith.tweaver.user.repository;

import com.valuewith.tweaver.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long> {
  Boolean existsByEmail(String email);

}
