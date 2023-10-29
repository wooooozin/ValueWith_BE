package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
import com.valuewith.tweaver.group.dto.TripGroupDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TripGroupService {

  private final GroupRepository groupRepository;
  private final DefaultImageRepository defaultImageRepository;

  public TripGroup writeGroup(TripGroupDto tripGroupDto) {
    // 사용자로 부터 사진 등록을 받지 못한 경우
    if(tripGroupDto.getThumbnailUrl() == null) {
      // 입력받은 지역으로 default_image에 있는 사진중에 랜덤 한장 뽑아서 저장
      tripGroupDto.setThumbnailUrl(getThumbnailUrl(tripGroupDto.getTripArea()));
    }

    TripGroup tripGroup = TripGroup.builder()
        .name(tripGroupDto.getName())
        .content(tripGroupDto.getContent())
        .maxUserNumber(tripGroupDto.getMaxUserNumber())
        .tripArea(tripGroupDto.getTripArea())
        .tripDate(tripGroupDto.getTripDate())
        .dueDate(tripGroupDto.getDueDate() == null ? tripGroupDto.getTripDate().minusDays(1)
            : tripGroupDto.getDueDate())
        .thumbnailUrl(tripGroupDto.getThumbnailUrl())
        .status(GroupStatus.OPEN)
        .build();
    groupRepository.save(tripGroup);

    return tripGroup;
  }

  public String getThumbnailUrl(String tripArea) {
    DefaultImage randomByImageName = defaultImageRepository.findRandomByImageName(tripArea);
    return randomByImageName.getDefaultImageUrl();
  }
}
