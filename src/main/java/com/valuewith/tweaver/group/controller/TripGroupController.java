package com.valuewith.tweaver.group.controller;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.constants.AlertContent;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.service.TripGroupService;
import com.valuewith.tweaver.groupMember.service.GroupMemberService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.service.MemberService;
import com.valuewith.tweaver.message.service.MessageService;
import com.valuewith.tweaver.place.service.PlaceService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class TripGroupController {
  private final MemberService memberService;
  private final TripGroupService tripGroupService;
  private final PlaceService placeService;
  private final ChatRoomService chatRoomService;
  private final GroupMemberService groupMemberService;
  private final MessageService messageService;
  private final TokenService tokenService;

  @ApiOperation(value = "여행 그룹 생성 API")
  @PostMapping
  public ResponseEntity<String> createGroup(
      @RequestPart(value = "tripGroupRequestDto") TripGroupRequestDto tripGroupRequestDto,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestHeader("Authorization") String token) {
    Member member = memberService.findMemberByEmail(tokenService.getMemberEmail(token));
    // 1.그룹 등록
    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto, file, member);
    // 2.여행 등록
    placeService.createPlace(tripGroup, tripGroupRequestDto.getPlaces());
    // 3.채팅 등록
    ChatRoom chatRoom = chatRoomService.createChatRoom(tripGroup);
    return ResponseEntity.ok("ok");
  }

  @ApiOperation(value = "여행 그룹 수정 API")
  @PutMapping
  public ResponseEntity<String> modifiedGroup(
      @RequestPart(value = "tripGroupRequestDto") TripGroupRequestDto tripGroupRequestDto,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart(value = "isDeletedFile") Boolean isDeletedFile) {
    // 1.그룹 수정
    TripGroup tripGroup = tripGroupService.modifiedTripGroup(tripGroupRequestDto, file, isDeletedFile);
    // 2.여행 수정
    placeService.modifiedPlace(tripGroup, tripGroupRequestDto.getPlaces());
    // 3.채팅 수정(그룹명 수정으로 인한 채팅룸 제목 수정)
    chatRoomService.modifiedChatRoom(tripGroup);
    // 4.그룹 멤버에게 그룹 수정에 대한 알림
    tripGroupService.sendTripGroupAlert(tripGroup.getTripGroupId(), AlertContent.UPDATE_GROUP_PLAN);
    return ResponseEntity.ok("ok");
  }

  @ApiOperation(value = "여행 그룹 삭제 API", notes = "이 API 호출 시 메세지/채팅룸/장소/여행그룹 이 동시에 삭제되고 그룹멤버에게 삭제 알림을 발송합니다.")
  @DeleteMapping("/{tripGroupId}")
  public ResponseEntity<String> deleteGroup(@PathVariable("tripGroupId") Long tripGroupId) {
    // 1.메세지 삭제
    messageService.deleteMessage(tripGroupId);
    // 2.채팅 삭제
    chatRoomService.deleteChatRoom(tripGroupId);
    // 3.일정 삭제
    placeService.deletePlaces(tripGroupId);
    // 4.그룹 삭제
    tripGroupService.deleteTripGroup(tripGroupId);
    // 5.그룹 멤버 삭제
    groupMemberService.deleteGroupMember(tripGroupId);
    // 6.그룹 멤버에게 그룹 삭제에 대한 알림
    tripGroupService.sendTripGroupAlert(tripGroupId, AlertContent.DELETED_GROUP);

    return ResponseEntity.ok("ok");
  }

}
