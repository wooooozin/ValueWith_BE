package com.valuewith.tweaver.place.service;

import com.valuewith.tweaver.group.Group;
import com.valuewith.tweaver.place.Place;
import com.valuewith.tweaver.place.dto.PlaceDto;
import com.valuewith.tweaver.place.repository.PlaceRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceServiceImpl implements PlaceService{
  private final PlaceRepository placeRepository;

  @Override
  public void writePlace(Group group, List<PlaceDto> placeDtos) {
    List<Place> places = new ArrayList<>();
    places.stream().map(place -> places.add(Place.builder()
        .name(place.getName())
        .x(place.getX())
        .y(place.getY())
        .address(place.getAddress())
        .placeCode(place.getPlaceCode())
        .orders(place.getOrders())
        .distance(place.getDistance())
        .group(group)
        .build()));
    placeRepository.saveAll(places);
  }
}
