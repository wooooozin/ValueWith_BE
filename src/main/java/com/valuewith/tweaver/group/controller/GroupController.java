package com.valuewith.tweaver.group.controller;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.group.dto.GroupDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.service.GroupService;
import com.valuewith.tweaver.member.service.MemberService;
import com.valuewith.tweaver.place.service.PlaceService;
import com.valuewith.tweaver.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("groups/*")
public class GroupController {

  private final GroupService groupService;
  private final PlaceService placeService;
  private final ChatRoomService chatRoomService;
  private final MemberService memberService;

  @PostMapping
  public ResponseEntity<?> writeGroup(GroupDto groupDto) {
    // 1.그룹 등록
    TripGroup tripGroup = groupService.writeGroup(groupDto);
    // 2.여행 등록
    placeService.writePlace(tripGroup, groupDto.getPlaces());
    // 3.채팅 등록
    ChatRoom chatRoom = chatRoomService.setChatRoom(tripGroup);
    // 4.멤버 등록(인증된 user값으로 등록) -> 일단 수기로 작성
    // TODO: spring security 인증작업이 끝나면 해당 기능 사용해서 현재 사용자정보 가져오기
    User user = User.builder()
        .userId(1L)
        .email("dodunge@gmail.com")
        .password("1234")
        .nickName("수정")
        .age(20)
        .gender("여성")
        .profileUrl("http://images...")
        .isSocial(true)
        .build();
    memberService.setMember(tripGroup, user, chatRoom);
    return ResponseEntity.ok("ok");
  }
}
