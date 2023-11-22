package com.valuewith.tweaver.message.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.message.dto.MessageDto;
import com.valuewith.tweaver.message.entity.Message;
import com.valuewith.tweaver.message.repository.MessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final ChatRoomRepository chatRoomRepository;
  private final MessageRepository messageRepository;

  public void deleteMessage(Long tripGroupId) {
    ChatRoom chatRoom = chatRoomRepository.findByTripGroupTripGroupId(tripGroupId)
        .orElseThrow(() -> new RuntimeException("삭제 하려는 메세지의 채팅방이 존재하지 않습니다."));
    messageRepository.deleteByChatRoomChatRoomId(chatRoom.getChatRoomId());
  }

  public MessageDto createMessage(ChatRoom chatRoom, Member sender, String content) {
    Message createdMessage = Message.from(chatRoom, sender, content);

    messageRepository.save(createdMessage);

    return MessageDto.from(createdMessage);
  }

  public List<MessageDto> findAllByMessage(Long memberId) {

    return messageRepository.findAllByMember_MemberId(memberId)
        .stream()
        .map(MessageDto::from)
        .collect(Collectors.toList());
  }
}
