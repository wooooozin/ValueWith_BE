package com.valuewith.tweaver.group.repository;

import com.valuewith.tweaver.group.entity.TripGroup;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripGroupRepositoryCustom {

    List<TripGroup> findFilteredTripGroups(String status, String area, String title, Pageable pageable);
    Long countFilteredTripGroups(String status, String area, String title);

    Page<TripGroup> findLeaderTripGroups(Long memberId, Pageable pageable);

    Page<TripGroup> findApprovedGroups(Long memberId, Pageable pageable);

    Page<TripGroup> findPendingGroups(Long memberId, Pageable pageable);

    /**
     * 매일 오전 12시 마감 처리
     */
    void updateTripGroupStatusToClose();
}
