package com.valuewith.tweaver.chat.controller;


import com.valuewith.tweaver.chat.dto.ChatRoomDto;
import com.valuewith.tweaver.chat.service.ChatMemberService;
import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatMemberService chatMemberService;
  private final MemberService memberService;

  @GetMapping("/room")
  public ResponseEntity<List<ChatRoomDto>> findChatRooms(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    Member member = memberService.findMemberByEmail(principalDetails.getUsername());
    List<ChatRoomDto> chatRoomList = chatMemberService.findMyGroupsByMemberId(member.getMemberId())
        .stream()
        .map(groupMember -> ChatRoomDto.from(groupMember.getChatRoom()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(chatRoomList);
  }
}
