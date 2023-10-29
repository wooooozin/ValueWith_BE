package com.valuewith.tweaver.place.service;

import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.place.dto.PlaceDto;
import com.valuewith.tweaver.place.entity.Place;
import com.valuewith.tweaver.place.repository.PlaceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

  private final PlaceRepository placeRepository;

  public void writePlace(TripGroup tripGroup, List<PlaceDto> placeDtos) {
    List<Place> places = placeDtos.stream().map(
            place -> Place.builder()
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
}
