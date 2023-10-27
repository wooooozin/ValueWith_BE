package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
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
  private final DefaultImageRepository defaultImageRepository;

  @Override
  public Group writeGroup(GroupDto groupDto) {
    // 사용자로 부터 사진 등록을 받지 못한 경우
    if(groupDto.getThumbnailUrl() == null) {
      // 입력받은 지역으로 default_image에 있는 사진중에 랜덤 한장 뽑아서 저장
      groupDto.setThumbnailUrl(getThumbnailUrl(groupDto.getTripArea()));
    }

    Group group = Group.builder()
        .name(groupDto.getName())
        .content(groupDto.getContent())
        .maxUserNumber(groupDto.getMaxUserNumber())
        .tripArea(groupDto.getTripArea())
        .tripDate(groupDto.getTripDate())
        .dueDate(groupDto.getDueDate() == null ? groupDto.getTripDate().minusDays(1)
            : groupDto.getDueDate())
        .thumbnailUrl(groupDto.getThumbnailUrl())
        .status(GroupStatus.OPEN)
        .build();
    groupRepository.save(group);

    return group;
  }

  public String getThumbnailUrl(String tripArea) {
    DefaultImage randomByImageName = defaultImageRepository.findRandomByImageName(tripArea);
    return randomByImageName.getDefaultImageUrl();
  }
}
