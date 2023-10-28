package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.group.dto.GroupDto;
import com.valuewith.tweaver.group.entity.TripGroup;

public interface GroupService {
  TripGroup writeGroup(GroupDto groupDto);

}
