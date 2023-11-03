package com.valuewith.tweaver.group.repository;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.group.entity.TripGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripGroupRepository extends JpaRepository<TripGroup, Long> {
    Page<TripGroup> findByStatusAndTripAreaAndNameContainingIgnoreCase(
        GroupStatus status, String area, String name, Pageable pageable
    );

}
