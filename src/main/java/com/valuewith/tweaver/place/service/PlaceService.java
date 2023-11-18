package com.valuewith.tweaver.place.service;

import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.place.dto.PlaceDto;
import com.valuewith.tweaver.place.entity.Place;
import com.valuewith.tweaver.place.repository.PlaceRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlaceService {

  private final PlaceDistanceService placeDistanceService;
  private final PlaceRepository placeRepository;

  public void createPlace(TripGroup tripGroup, List<PlaceDto> placeDtos) {
    List<PlaceDto> placeIncludeDistance = placeDistanceService.setDistancesFromApi(placeDtos);
    List<Place> places = placeIncludeDistance.stream().map(
            place -> Place.builder()
                .category(place.getCategory())
                .name(place.getName())
                .x(place.getX())
                .y(place.getY())
                .address(place.getAddress())
                .placeCode(place.getPlaceCode())
                .orders(place.getOrders())
                .distance(place.getDistance())
                .tripGroup(tripGroup)
                .build())
        .collect(Collectors.toList());
    placeRepository.saveAll(places);
  }

  /**
   * 기존 장소 데이터를 지우고 새로 등록
   * @param placeDtos
   */
  public void modifiedPlace(TripGroup tripGroup, List<PlaceDto> placeDtos) {
    placeRepository.deletePlacesByTripGroup(tripGroup);

    createPlace(tripGroup, placeDtos);
  }

  public void deletePlaces(Long tripGroupId) {
    placeRepository.deleteAllByTripGroupTripGroupId(tripGroupId);
  }
}
