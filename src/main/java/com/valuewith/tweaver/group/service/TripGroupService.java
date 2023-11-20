package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.alert.dto.AlertRequestDto;
import com.valuewith.tweaver.alert.service.AlertService;
import com.valuewith.tweaver.constants.AlertContent;
import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.member.entity.Member;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TripGroupService {
  private final ImageService imageService;
  private final AlertService alertService;

  private final TripGroupRepository tripGroupRepository;
  private final DefaultImageRepository defaultImageRepository;
  private final GroupMemberRepository groupMemberRepository;

  public TripGroup createTripGroup(TripGroupRequestDto tripGroupRequestDto, MultipartFile file, Member member) {

    if (file != null && !file.isEmpty()) {
      String imageUrl = imageService.uploadImageAndGetUrl(file, ImageType.THUMBNAIL);
      tripGroupRequestDto.setThumbnailUrl(imageUrl);
    } else {
      // 사용자로 부터 사진 등록을 받지 못한 경우
      // 입력받은 지역으로 default_image에 있는 사진중에 랜덤 한장 뽑아서 저장
      tripGroupRequestDto.setThumbnailUrl(getThumbnailUrl(tripGroupRequestDto.getTripArea()));
    }

    TripGroup tripGroup = TripGroup.builder()
        .member(member)
        .name(tripGroupRequestDto.getName())
        .content(tripGroupRequestDto.getContent())
        .maxMemberNumber(tripGroupRequestDto.getMaxMemberNumber())
        .currentMemberNumber(0)
        .tripArea(tripGroupRequestDto.getTripArea())
        .tripDate(tripGroupRequestDto.getTripDate())
        .dueDate(tripGroupRequestDto.getDueDate() == null ? tripGroupRequestDto.getTripDate().minusDays(1)
            : tripGroupRequestDto.getDueDate())
        .thumbnailUrl(tripGroupRequestDto.getThumbnailUrl())
        .status(GroupStatus.OPEN)
        .build();

    return tripGroupRepository.save(tripGroup);
  }

  public TripGroup modifiedTripGroup(TripGroupRequestDto tripGroupRequestDto, MultipartFile file) {

    TripGroup foundTripGroup = tripGroupRepository.findById(tripGroupRequestDto.getTripGroupId()).orElseThrow(() -> {
      throw new RuntimeException("수정할 그룹 데이터가 존재하지 않습니다.");
    });

    if (file != null && !file.isEmpty()) {
      String imageUrl = imageService.modifiedImageWithFallback(file, foundTripGroup.getThumbnailUrl(), ImageType.THUMBNAIL);
      tripGroupRequestDto.setThumbnailUrl(imageUrl);
    }

    foundTripGroup.updateTripGroup(tripGroupRequestDto);

    return foundTripGroup;
  }

  public String getThumbnailUrl(String tripArea) {
    DefaultImage randomByImageName = defaultImageRepository.findRandomByImageName(tripArea);
    return randomByImageName.getDefaultImageUrl();
  }

  public void deleteTripGroup(Long tripGroupId) {
    tripGroupRepository.deleteById(tripGroupId);
  }

  public void sendTripGroupAlert(Long tripGroupId, AlertContent alertContent) {
    List<GroupMember> groupMembers
        = groupMemberRepository.findApprovedMembersByTripGroupId(tripGroupId);

    groupMembers.stream().forEach(groupMember -> {
      alertService.send(AlertRequestDto.builder()
          .groupId(tripGroupId)
          .member(groupMember.getMember())
          .content(alertContent)
          .build());
    });


  }
}
