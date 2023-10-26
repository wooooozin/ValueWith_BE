package com.valuewith.tweaver.place.service;

import com.valuewith.tweaver.group.Group;
import com.valuewith.tweaver.place.dto.PlaceDto;
import java.util.List;

public interface PlaceService {
  void writePlace(Group group, List<PlaceDto> placeDtos);
}
