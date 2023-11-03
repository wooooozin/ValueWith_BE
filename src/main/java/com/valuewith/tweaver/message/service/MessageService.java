package com.valuewith.tweaver.message.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
  private final ChatRoomRepository chatRoomRepository;
  private final MessageRepository messageRepository;

  public void deleteMessage(Long tripGroupId) {
    ChatRoom chatRoom = chatRoomRepository.findByTripGroupTripGroupId(tripGroupId)
        .orElseThrow(() -> new RuntimeException("삭제 하려는 메세지의 채팅방이 존재하지 않습니다."));
    messageRepository.deleteByChatRoomChatRoomId(chatRoom.getChatRoomId());
  }

}
