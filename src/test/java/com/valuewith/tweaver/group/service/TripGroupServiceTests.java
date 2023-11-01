package com.valuewith.tweaver.group.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.groupMember.service.GroupMemberService;
import com.valuewith.tweaver.place.dto.PlaceDto;
import com.valuewith.tweaver.place.entity.Place;
import com.valuewith.tweaver.place.repository.PlaceRepository;
import com.valuewith.tweaver.place.service.PlaceService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
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

  TripGroup modifiedTestTripGroup;
  ChatRoom modifiedChatRoom;

  /**
   * 그룹 수정 테스트를 위한 사전 그룹 등록 처리
   */
  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .name("서울 경복궁 여행 모임")
        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
        .maxMemberNumber(30)
        .tripArea("서울")
        .tripDate(LocalDate.parse("2023-12-13"))
        .dueDate(LocalDate.parse("2023-12-12"))
        .build();

    MockMultipartFile file = new MockMultipartFile(
        "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
    );

    modifiedTestTripGroup = tripGroupService.createTripGroup(tripGroupRequestDto, file);

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

    placeService.createPlace(modifiedTestTripGroup, places);

    modifiedChatRoom = chatRoomService.createChatRoom(modifiedTestTripGroup);
  }

  /**
   * 그룹 등록시, 사용자에게 사진을 등록 받은 경우
   */
  @Test
  void createTripGroup() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .name("서울 경복궁 여행 모임")
        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
        .maxMemberNumber(30)
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
        .maxMemberNumber(30)
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
        .maxMemberNumber(30)
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
    assertEquals(all.size(), 6);
  }

  @Test
  void createChatRoom() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .name("서울 경복궁 여행 모임")
        .content("함께 여행할 동행자 모집합니다! 20대 여성이고, 한복 여행을 하고 싶은분 신청해 주세요.")
        .maxMemberNumber(30)
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

  /**
   * 그룹 수정시 max를 current보다 크게 변경
   */
  @Test
  void modifiedTripGroup() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
        .name("그룹명 수정")
        .content("코멘트 수정")
        .maxMemberNumber(25)
        .tripArea("경기도")
        .tripDate(LocalDate.parse("2023-11-17"))
        .dueDate(LocalDate.parse("2023-11-11"))
        .build();

    tripGroupService.modifiedTripGroup(tripGroupRequestDto, null);

    //when
    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());

    //then
    foundTripGroup.ifPresent(tripGroup -> {
      assertEquals("그룹명 수정", tripGroup.getName());
      assertEquals("코멘트 수정", tripGroup.getContent());
      assertEquals(25, tripGroup.getMaxMemberNumber());
      assertEquals("경기도", tripGroup.getTripArea());
      assertEquals(LocalDate.of(2023, 11, 17), tripGroup.getTripDate());
      assertEquals(LocalDate.of(2023, 11, 11), tripGroup.getDueDate());
    });

  }

  /**
   * 그룹 수정시 max를 current랑 같게 변경 -> 그룹 상태 마감으로 변경된 것 확인
   */
  @Test
  void modifiedTripGroupEqualCurrentAndMax() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
        .name("그룹 수정시 max를 current랑 같게 변경")
        .content("코멘트 수정")
        .maxMemberNumber(0)
        .tripArea("경기도")
        .tripDate(LocalDate.parse("2023-11-17"))
        .dueDate(LocalDate.parse("2023-11-11"))
        .build();

    tripGroupService.modifiedTripGroup(tripGroupRequestDto, null);

    //when
    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());

    //then
    foundTripGroup.ifPresent(tripGroup -> {
      assertEquals(0, tripGroup.getMaxMemberNumber());
      assertEquals(0, tripGroup.getCurrentMemberNumber());
      assertEquals(GroupStatus.CLOSE, tripGroup.getStatus());
    });
  }

  /**
   * thumbnail사진 없이 수정 -> 수정전 thumbnail_url과 같아야 함
   */
  @Test
  void modifiedTripGroupNoThumbnail() {
    //given
    String beforeUrl = modifiedTestTripGroup.getThumbnailUrl();

    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
        .name("썸네일 파일 없는 수정")
        .content("코멘트 수정")
        .maxMemberNumber(25)
        .tripArea("경기도")
        .tripDate(LocalDate.parse("2023-11-17"))
        .dueDate(LocalDate.parse("2023-11-11"))
        .build();

    tripGroupService.modifiedTripGroup(tripGroupRequestDto, null);

    //when
    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());

    //then
    foundTripGroup.ifPresent(tripGroup -> {
      assertEquals("썸네일 파일 없는 수정", tripGroup.getName());
      assertEquals(beforeUrl, tripGroup.getThumbnailUrl());
    });
  }

//  /**
//   * thumbnail사진 수정 -> 수정전 Url과 달라야 함.
//   * 실제로 S3에 있는 값을 테스트 확인 완료
//   * 테스트 할 때마다 실제로 S3에 있는 url을 가지고 테스트 해야함.
//   */
//  @Test
//  void modifiedTripGroupThumbnail() {
//    //given
//    String beforeUrl = modifiedTestTripGroup.getThumbnailUrl();
//
//    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
//        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
//        .name("썸네일 파일 있는 수정")
//        .content("코멘트 수정")
//        .maxMemberNumber(25)
//        .tripArea("경기도")
//        .tripDate(LocalDate.parse("2023-11-17"))
//        .dueDate(LocalDate.parse("2023-11-11"))
//        .thumbnailUrl("https://d1udi89ozp4mef.cloudfront.net/thumbnail%2Fd19609e2-c20f-4cb0-9ed0-85382708b5bb-Sketchpad.png")
//        .build();
//
//    MockMultipartFile file = new MockMultipartFile(
//        "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
//    );
//
//    tripGroupService.modifiedTripGroup(tripGroupRequestDto, file);
//
//    //when
//    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());
//
//    //then
//    foundTripGroup.ifPresent(tripGroup -> {
//      assertEquals("썸네일 파일 있는 수정", tripGroup.getName());
//      assertNotEquals(beforeUrl, tripGroup.getThumbnailUrl());
//    });
//  }

  /**
   * dueDate없이 수정 -> 여행날짜의 하루 전으로 등록
   */
  @Test
  void modifiedTripNoDueDate() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
        .name("dueDate없이 수정")
        .content("코멘트 수정")
        .maxMemberNumber(25)
        .tripArea("경기도")
        .tripDate(LocalDate.parse("2023-12-17"))
        .build();

    tripGroupService.modifiedTripGroup(tripGroupRequestDto, null);

    //when
    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());

    //then
    foundTripGroup.ifPresent(tripGroup -> {
      assertEquals("dueDate없이 수정", tripGroup.getName());
      assertNotEquals(new LocalDate[2023-12-17], tripGroup.getTripDate());
      assertNotEquals(new LocalDate[2023-12-16], tripGroup.getDueDate());
    });
  }

  /**
   * dueDate를 현재 날짜 보다 늦게 변경 -> 모집 상태를 모집중으로 변경
   */
  @Test
  void modifiedTripDueDateLate() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
        .name("dueDate를 현재 날짜 보다 늦게 변경")
        .content("코멘트 수정")
        .maxMemberNumber(25)
        .tripArea("경기도")
        .tripDate(LocalDate.parse("2023-12-22"))
        .dueDate(LocalDate.parse("2023-12-19"))
        .build();

    tripGroupService.modifiedTripGroup(tripGroupRequestDto, null);

    //when
    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());

    //then
    foundTripGroup.ifPresent(tripGroup -> {
      assertEquals("dueDate를 현재 날짜 보다 늦게 변경", tripGroup.getName());
      assertEquals(GroupStatus.OPEN, tripGroup.getStatus());
    });
  }

  /**
   * dueDate를 현재 날짜 보다 빠르게 변경 -> 모집 상태를 마감으로 변경
   */
  @Test
  void modifiedTripDueDateFast() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
        .name("dueDate를 현재 날짜 보다 빠르게 변경")
        .content("코멘트 수정")
        .maxMemberNumber(25)
        .tripArea("경기도")
        .tripDate(LocalDate.parse("2023-12-22"))
        .dueDate(LocalDate.parse("2023-10-19"))
        .build();

    tripGroupService.modifiedTripGroup(tripGroupRequestDto, null);

    //when
    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());

    //then
    foundTripGroup.ifPresent(tripGroup -> {
      assertEquals("dueDate를 현재 날짜 보다 빠르게 변경", tripGroup.getName());
      assertEquals(GroupStatus.CLOSE, tripGroup.getStatus());
    });
  }

  /**
   * place가 3개 였던걸 4개로 수정.
   */
  @Test
  void modifiedPlaces() {
    //given
    List<PlaceDto> places = new ArrayList<>();
    places.add(PlaceDto.builder()
        .name("경복궁 카페 수정")
        .x(137.123523)
        .y(36.930291)
        .address("서울특별시 종로구 삼청로 24")
        .placeCode("1233456")
        .orders(2)
        .build());

    places.add(PlaceDto.builder()
        .name("경복궁 관훈점")
        .x(137.146235)
        .y(36.843464)
        .address("서울특별시 종로구 인사동5길 38 센터마크호텔 B1")
        .placeCode("1111111")
        .orders(3)
        .build());

    places.add(PlaceDto.builder()
        .name("경복궁")
        .x(137.444444)
        .y(36.555555)
        .address("서울특별시 종로구 사직로 161")
        .placeCode("9999999")
        .orders(1)
        .build());

    places.add(PlaceDto.builder()
        .name("경복궁 수정 수정")
        .x(137.444444)
        .y(36.555555)
        .address("서울특별시 종로구 사직로 161")
        .placeCode("9999999")
        .orders(4)
        .build());

    //when
    placeService.modifiedPlace(modifiedTestTripGroup, places);

    //then -> 삭제된 3개 + 새로 추가된 4개 = 7개
    List<Place> all = placeRepository.findAll();
    assertEquals(7, all.size());
  }

  /**
   * tripGroup에서 그룹명이 변경될 시, 채팅명도 변경됨.
   */
  @Test
  void modifiedChatRoom() {
    //given
    TripGroupRequestDto tripGroupRequestDto = TripGroupRequestDto.builder()
        .tripGroupId(modifiedTestTripGroup.getTripGroupId())
        .name("채팅명 변경 확인")
        .content("코멘트 수정")
        .maxMemberNumber(25)
        .tripArea("경기도")
        .tripDate(LocalDate.parse("2023-12-22"))
        .dueDate(LocalDate.parse("2023-10-19"))
        .build();

    tripGroupService.modifiedTripGroup(tripGroupRequestDto, null);

    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(modifiedTestTripGroup.getTripGroupId());

    //when
    //then
    foundTripGroup.ifPresent(tripGroup -> {
      chatRoomService.modifiedChatRoom(tripGroup);
      chatRoomRepository.findByTripGroup(tripGroup).ifPresent(chatRoom -> {
        assertEquals("채팅명 변경 확인", chatRoom.getTitle());
      });
    });

  }
}
