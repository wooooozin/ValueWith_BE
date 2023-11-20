package com.valuewith.tweaver.message.controller;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.service.MemberService;
import com.valuewith.tweaver.message.dto.MessageDto;
import com.valuewith.tweaver.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

  private final ChatRoomService chatRoomService;
  private final MemberService memberService;
  private final MessageService messageService;
  private final SimpMessageSendingOperations simpMessageSendingOperations;

  @MessageMapping("/message/join/{chatRoomId}/{memberId}")
  public void enterMember(@DestinationVariable("chatRoomId") Long chatRoomId,
      @DestinationVariable("memberId") Long memberId) {
    ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
    Member sender = memberService.findMemberByMemberId(memberId);
    String helloMessage =
        sender.getNickName() + "님이 " + chatRoom.getTripGroup().getName() + "그룹에 참여하셨습니다.";

    MessageDto newMessage = messageService.createMessage(chatRoom, sender, helloMessage);

    simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatRoom.getChatRoomId(),
        newMessage);
  }

  @MessageMapping("/message/{chatRoomId}")
  public void sendMessage(@Payload MessageDto message,
      @DestinationVariable("chatRoomId") Long chatRoomId) {
    ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
    Member sender = memberService.findMemberByMemberId(message.getMemberIdDto().getMemberId());

    MessageDto newMessage = messageService.createMessage(chatRoom, sender, message.getContent());

    simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatRoom.getChatRoomId(),
        newMessage);
  }

  @MessageMapping("/message/exit/{chatRoomId}/{memberId}")
  public void exitMember(@DestinationVariable("chatRoomId") Long chatRoomId,
      @DestinationVariable("memberId") Long memberId) {
    ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
    Member sender = memberService.findMemberByMemberId(memberId);
    String byeMessage =
        sender.getNickName() + "님이 " + chatRoom.getTripGroup().getName() + "그룹에서 나가셨습니다.";

    MessageDto newMessage = messageService.createMessage(chatRoom, sender, byeMessage);

    simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatRoom.getChatRoomId(),
        newMessage);
  }
}
