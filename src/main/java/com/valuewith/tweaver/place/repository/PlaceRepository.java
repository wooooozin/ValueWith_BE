package com.valuewith.tweaver.place.repository;

import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.place.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
  List<Place> deletePlacesByTripGroup(TripGroup tripGroup);
}
