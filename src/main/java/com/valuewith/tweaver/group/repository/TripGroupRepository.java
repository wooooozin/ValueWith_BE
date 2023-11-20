package com.valuewith.tweaver.group.repository;

import com.valuewith.tweaver.group.entity.TripGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripGroupRepository extends JpaRepository<TripGroup, Long>, TripGroupRepositoryCustom {
    Optional<TripGroup> findByTripGroupId(Long tripGroupId);

    List<TripGroup> findTripGroupsByMember_MemberId(Long memberId);
}
