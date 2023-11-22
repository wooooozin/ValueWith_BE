package com.valuewith.tweaver.chat.controller;


import static com.valuewith.tweaver.constants.ErrorCode.NOT_A_MEMBER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.chat.dto.ChatRoomDto;
import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.service.ChatMemberService;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.exception.CustomException;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.service.TripGroupService;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.service.GroupMemberService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatMemberService chatMemberService;
  private final ChatRoomService chatRoomService;
  private final MemberService memberService;
  private final GroupMemberService groupMemberService;
  private final TripGroupService tripGroupService;
  private final ObjectMapper objectMapper;

  @GetMapping("/room")
  public ResponseEntity<List<ChatRoomDto>> findChatRooms(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    Member member = memberService.findMemberByEmail(principalDetails.getUsername());
    // 1. 그룹원인 경우
    List<ChatRoomDto> memberChat = groupMemberService.findApprovedGroupsByMemberId(
            member.getMemberId())
        .stream()
        .map(groupMember -> ChatRoomDto.from(groupMember.getChatRoom()))
        .collect(Collectors.toList());

    // 2. 그룹장일 경우
    List<ChatRoomDto> leaderChat = tripGroupService.findMyTripGroupListByMemberId(member.getMemberId())
        .stream()
        .map(TripGroup::getTripGroupId)
        .map(chatRoomService::findByChatRoomId)
        .map(ChatRoomDto::from)
        .collect(Collectors.toList());

    // 3. 전부 통합
    List<ChatRoomDto> chatRoomList = ListUtils.union(memberChat, leaderChat);

    return ResponseEntity.ok(chatRoomList);
  }

  @PostMapping("/room/{chatRoomId}")
  public ResponseEntity<String> enterChatRoom(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @PathVariable Long chatRoomId) {

    // 팀장일때는 TrpiGroupId로 찾아야함!!
    ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
    Long memberId = memberService.findMemberByEmail(principalDetails.getUsername()).getMemberId();
    Long tripGroupId = chatRoom.getTripGroup().getTripGroupId();

    if (!groupMemberService.isGroupMember(memberId, tripGroupId)) {
      throw new CustomException(NOT_A_MEMBER);
    }

    GroupMember groupMember = groupMemberService.findGroupMemberByMemberIdAndGroupId(
        memberId, tripGroupId);

    return ResponseEntity.ok(chatMemberService.enterChatRoom(chatRoom, groupMember));
  }

  @DeleteMapping("/room/{chatRoomId}")
  public ResponseEntity<String> exitChatRoom(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @PathVariable Long chatRoomId
  ) {
    ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
    Long memberId = memberService.findMemberByEmail(principalDetails.getUsername()).getMemberId();
    Long tripGroupId = chatRoom.getTripGroup().getTripGroupId();
    GroupMember groupMember = groupMemberService.findGroupMemberByMemberIdAndGroupId(
        memberId, tripGroupId);

    return ResponseEntity.ok(chatMemberService.exitChatRoom(chatRoom, groupMember));
  }
}
