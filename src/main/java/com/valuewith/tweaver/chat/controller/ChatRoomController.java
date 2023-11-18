package com.valuewith.tweaver.chat.controller;


import com.valuewith.tweaver.chat.dto.ChatRoomDto;
import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.service.ChatMemberService;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.service.GroupMemberService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
  private final GroupMemberService groupMemberService;
  private final MemberService memberService;

  @GetMapping("/room")
  public ResponseEntity<List<ChatRoomDto>> findChatRooms(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    Member member = memberService.findMemberByEmail(principalDetails.getUsername());
    List<ChatRoomDto> chatRoomList = groupMemberService.findApprovedGroupsByMemberId(
            member.getMemberId())
        .stream()
        .map(groupMember -> ChatRoomDto.from(groupMember.getChatRoom()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(chatRoomList);
  }

  @PostMapping("/room/{chatRoomId}")
  public ResponseEntity<String> enterChatRoom(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @PathVariable Long chatRoomId) {
    ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
    Long memberId = memberService.findMemberByEmail(principalDetails.getUsername()).getMemberId();
    Long tripGroupId = chatRoom.getTripGroup().getTripGroupId();

    // TODO: 해당 그룹원이 아니면 커스텀 익셉션
    if (!groupMemberService.isGroupMember(memberId, tripGroupId)) {
      throw new RuntimeException("해당 그룹원이 아닙니다.");
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
