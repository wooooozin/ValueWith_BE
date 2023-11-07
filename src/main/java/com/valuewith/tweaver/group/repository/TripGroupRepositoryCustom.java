package com.valuewith.tweaver.group.repository;

import com.valuewith.tweaver.group.entity.TripGroup;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TripGroupRepositoryCustom {

    List<TripGroup> findFilteredTripGroups(String status, String area, String title, Pageable pageable);
    long countFilteredTripGroups(String status, String area, String title);

}
