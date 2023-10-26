package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.group.dto.GroupDto;
import com.valuewith.tweaver.group.entity.Group;
import com.valuewith.tweaver.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;

  @Override
  public Group writeGroup(GroupDto groupDto) {
    Group group = Group.builder()
        .name(groupDto.getName())
        .content(groupDto.getContent())
        .maxUserNumber(groupDto.getMaxUserNumber())
        .tripArea(groupDto.getTripArea())
        .tripDate(groupDto.getTripDate())
        .dueDate(groupDto.getDueDate() == null ? groupDto.getTripDate().minusDays(1)
            : groupDto.getDueDate())
        .snapshotUrl(groupDto.getSnapshotUrl())
        .status(GroupStatus.OPEN)
        .build();
    groupRepository.save(group);

    return null;
  }
}
