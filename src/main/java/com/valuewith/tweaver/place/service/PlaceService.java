package com.valuewith.tweaver.place.service;

import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.place.dto.PlaceDto;
import java.util.List;

public interface PlaceService {
  void writePlace(TripGroup tripGroup, List<PlaceDto> placeDtos);
}
