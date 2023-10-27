package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.group.dto.GroupDto;
import com.valuewith.tweaver.group.entity.Group;

public interface GroupService {
  Group writeGroup(GroupDto groupDto);

}
