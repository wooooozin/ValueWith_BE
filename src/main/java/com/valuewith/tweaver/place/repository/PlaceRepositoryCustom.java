package com.valuewith.tweaver.place.repository;

import com.valuewith.tweaver.place.entity.Place;
import java.util.List;

public interface PlaceRepositoryCustom {
    List<Place> findByTripGroupId(Long tripGroupId);
}
