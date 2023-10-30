package com.valuewith.tweaver.group.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.groupMember.service.GroupMemberService;
import com.valuewith.tweaver.menber.entity.Member;
import com.valuewith.tweaver.place.dto.PlaceDto;
import com.valuewith.tweaver.place.entity.Place;
import com.valuewith.tweaver.place.repository.PlaceRepository;
import com.valuewith.tweaver.place.service.PlaceService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TripGroupServiceTests {
  @Autowired
  TripGroupRepository tripGroupRepository;
  @Autowired
  PlaceRepository placeRepository;
  @Autowired
  ChatRoomRepository chatRoomRepository;
  @Autowired
  GroupMemberRepository groupMemberRepository;

  @Autowired
  TripGroupService tripGroupService;
  @Autowired
  PlaceService placeService;
  @Autowired
  ChatRoomService chatRoomService;
  @Autowired
  GroupMemberService groupMemberService;

  /**
   * 그룹 등록시, 사용자에게 사진을 등록 받은 경우
   */
  @Test
  void createTripGroup() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .name("서울 경복궁 여행 모임")
        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
        .maxUserNumber(30)
        .tripArea("서울")
        .tripDate(LocalDate.parse("2023-12-13"))
        .dueDate(LocalDate.parse("2023-12-12"))
        .build();

    MockMultipartFile file = new MockMultipartFile(
        "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
    );

    //when
    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto, file);

    //then
    assertNotNull(tripGroup);
  }

  /**
   * 그룹 등록시, 마감날짜(dueDate) 입력 안하기
   * 여행 날짜 하루 전 날짜로 자동 입력됨
   */
  @Test
  void createTripGroupNoDueDate() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .name("서울 경복궁 여행 모임")
        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
        .maxUserNumber(30)
        .tripArea("서울")
        .tripDate(LocalDate.parse("2023-12-23"))
        .thumbnailUrl("https://")
        .build();

    MockMultipartFile file = new MockMultipartFile(
        "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
    );

    //when
    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto, file);

    //then
    assertEquals(tripGroup.getDueDate(), LocalDate.parse("2023-12-22"));
  }

//  /**
//   * 그룹 등록시, 사용자에게 사진을 등록 받지 않은 경우
//   * 서비스에서 지역별로 사진을 등록해줌.
//   * TODO: 지역별 등록 메소드 완성 후 재검증
//   */
//  @Test
//  void createTripGroupNoThumbnailUrl() {
//    //given
//    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
//        .name("서울 경복궁 여행 모임")
//        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
//        .maxUserNumber(30)
//        .tripArea("서울")
//        .tripDate(LocalDate.parse("2023-12-13"))
//        .dueDate(LocalDate.parse("2023-12-12"))
//        .build();
//
//    //when
//    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto);
//
//    //then
//    assertNotNull(tripGroup.getThumbnailUrl());
//  }

  /**
   * place 3개 등록하고 확인해 봤을 때 등록한 place 개수가 3으로 나옴.
   */
  @Test
  void createPlace() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .name("서울 경복궁 여행 모임")
        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
        .maxUserNumber(30)
        .tripArea("서울")
        .tripDate(LocalDate.parse("2023-12-13"))
        .dueDate(LocalDate.parse("2023-12-12"))
        .thumbnailUrl("https://")
        .build();

    MockMultipartFile file = new MockMultipartFile(
        "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
    );

    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto, file);

    List<PlaceDto> places = new ArrayList<>();
    places.add(PlaceDto.builder()
        .name("경복궁 카페")
        .x(137.123523)
        .y(36.930291)
        .address("서울특별시 종로구 삼청로 24")
        .placeCode("1233456")
        .orders(3)
        .build());

    places.add(PlaceDto.builder()
        .name("경복궁 관훈점")
        .x(137.146235)
        .y(36.843464)
        .address("서울특별시 종로구 인사동5길 38 센터마크호텔 B1")
        .placeCode("1111111")
        .orders(1)
        .build());

    places.add(PlaceDto.builder()
        .name("경복궁")
        .x(137.444444)
        .y(36.555555)
        .address("서울특별시 종로구 사직로 161")
        .placeCode("9999999")
        .orders(2)
        .build());

    //when
    placeService.createPlace(tripGroup, places);

    //then
    List<Place> all = placeRepository.findAll();
    assertEquals(all.size(), 3);
  }

  @Test
  void createChatRoom() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .name("서울 경복궁 여행 모임")
        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
        .maxUserNumber(30)
        .tripArea("서울")
        .tripDate(LocalDate.parse("2023-12-13"))
        .dueDate(LocalDate.parse("2023-12-12"))
        .thumbnailUrl("https://")
        .build();

    MockMultipartFile file = new MockMultipartFile(
        "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
    );

    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto, file);

    //when
    ChatRoom chatRoom = chatRoomService.createChatRoom(tripGroup);

    //then
    assertNotNull(chatRoom);

  }

//  /**
//   * TODO: member 등록 메소드 완성시 재검증
//   */
//  @Test
//  void createGroupMember() {
//    //given
//    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
//        .name("서울 경복궁 여행 모임")
//        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
//        .maxUserNumber(30)
//        .tripArea("서울")
//        .tripDate(LocalDate.parse("2023-12-13"))
//        .dueDate(LocalDate.parse("2023-12-12"))
//        .thumbnailUrl("https://")
//        .build();
//    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto);
//
//    // member_id가 3L인 데이터 하나를 만들어서 사용
//    Member member = Member.builder()
//        .memberId(3L)
//        .email("dodunge@gmail.com")
//        .password("1234")
//        .nickName("수정")
//        .age(30)
//        .gender("여성")
//        .profileUrl("http://images...")
//        .isSocial(true)
//        .build();
//
//    ChatRoom chatRoom = chatRoomService.createChatRoom(tripGroup);
//
//    //when
//    groupMemberService.createGroupMember(tripGroup, member, chatRoom);
//
//    //then
//    GroupMember groupMember = groupMemberRepository.findAll().get(0);
//    assertNotNull(groupMember);
//
//  }
}
